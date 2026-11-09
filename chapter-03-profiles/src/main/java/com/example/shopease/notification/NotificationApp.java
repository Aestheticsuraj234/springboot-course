package com.example.shopease.notification;

import com.example.shopease.notification.channel.ConsoleNotificationSender;
import com.example.shopease.notification.channel.EmailNotificationSender;
import com.example.shopease.notification.channel.NotificationSender;
import com.example.shopease.notification.channel.NotificationSenderRegistry;
import com.example.shopease.notification.channel.SmsNotificationSender;
import com.example.shopease.notification.config.AppConfig;
import com.example.shopease.notification.config.ClasspathPropertiesLoader;
import com.example.shopease.notification.order.OrderConfirmationService;
import java.util.List;

/** Composition root: reads the profile-specific config and wires the matching sender. */
public final class NotificationApp {

    private NotificationApp() {
    }

    public static void main(String[] args) {
        AppConfig config = AppConfig.from(new ClasspathPropertiesLoader().load("application.properties"));

        NotificationSenderRegistry registry = new NotificationSenderRegistry(List.of(
                new ConsoleNotificationSender(System.out),
                new EmailNotificationSender(config.senderAddress(), System.out),
                new SmsNotificationSender(config.senderAddress(), System.out)));
        NotificationSender sender = registry.senderFor(config.notificationChannel());

        System.out.println("Environment     : " + config.environment());
        System.out.println("Channel         : " + sender.channel());
        System.out.println("Payment gateway : " + config.paymentGatewayUrl());
        System.out.println();

        new OrderConfirmationService(sender).confirm("ORD-20261109-7781", "+91-98XXXXXX21");
    }
}
