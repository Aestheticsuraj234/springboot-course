package com.example.shopease.notification.channel;

/** Delivers a notification over one channel (console, email, SMS, push...). */
public interface NotificationSender {

    /** The channel name used in configuration, e.g. {@code email}. */
    String channel();

    void send(Notification notification);
}
