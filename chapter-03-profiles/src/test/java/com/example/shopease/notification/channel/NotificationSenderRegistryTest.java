package com.example.shopease.notification.channel;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.Test;

class NotificationSenderRegistryTest {

    private final PrintStream sink = new PrintStream(new ByteArrayOutputStream());
    private final NotificationSender sms = new SmsNotificationSender("ShopEase", sink);
    private final NotificationSenderRegistry registry =
            new NotificationSenderRegistry(List.of(new ConsoleNotificationSender(sink), sms));

    @Test
    void findsSenderByChannel() {
        assertSame(sms, registry.senderFor("sms"));
    }

    @Test
    void rejectsUnknownChannel() {
        assertThrows(IllegalArgumentException.class, () -> registry.senderFor("pigeon"));
    }

    @Test
    void rejectsTwoSendersForOneChannel() {
        assertThrows(IllegalArgumentException.class, () -> new NotificationSenderRegistry(
                List.of(new ConsoleNotificationSender(sink), new ConsoleNotificationSender(sink))));
    }
}
