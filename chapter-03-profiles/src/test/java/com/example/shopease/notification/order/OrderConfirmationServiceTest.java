package com.example.shopease.notification.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.shopease.notification.channel.Notification;
import com.example.shopease.notification.channel.NotificationSender;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderConfirmationServiceTest {

    /** Hand-written fake: records what would have been sent. */
    private static final class RecordingSender implements NotificationSender {
        final List<Notification> sent = new ArrayList<>();

        @Override
        public String channel() {
            return "recording";
        }

        @Override
        public void send(Notification notification) {
            sent.add(notification);
        }
    }

    @Test
    void sendsConfirmationToCustomer() {
        RecordingSender sender = new RecordingSender();

        new OrderConfirmationService(sender).confirm("ORD-1", "asha@example.com");

        assertEquals(1, sender.sent.size());
        Notification sent = sender.sent.get(0);
        assertEquals("asha@example.com", sent.recipient());
        assertEquals("Order ORD-1 confirmed", sent.subject());
        assertTrue(sent.body().contains("ORD-1"));
    }
}
