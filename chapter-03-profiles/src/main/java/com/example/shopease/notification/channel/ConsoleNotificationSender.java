package com.example.shopease.notification.channel;

import java.io.PrintStream;
import java.util.Objects;

/** For local development: prints instead of delivering, so nobody gets a real message. */
public final class ConsoleNotificationSender implements NotificationSender {

    private final PrintStream out;

    public ConsoleNotificationSender(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out");
    }

    @Override
    public String channel() {
        return "console";
    }

    @Override
    public void send(Notification notification) {
        out.printf("[console] to=%s | %s | %s%n", notification.recipient(), notification.subject(), notification.body());
    }
}
