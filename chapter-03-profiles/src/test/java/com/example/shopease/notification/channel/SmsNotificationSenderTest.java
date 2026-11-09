package com.example.shopease.notification.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SmsNotificationSenderTest {

    @Test
    void keepsShortMessagesIntact() {
        assertEquals("Hi: there", SmsNotificationSender.text(new Notification("+91", "Hi", "there")));
    }

    @Test
    void truncatesToOneSmsSegment() {
        String text = SmsNotificationSender.text(new Notification("+91", "Order", "x".repeat(500)));

        assertEquals(SmsNotificationSender.MAX_LENGTH, text.length());
        assertTrue(text.endsWith("..."));
    }
}
