package com.example.shopease.notification.channel;

import java.io.PrintStream;
import java.util.Objects;

/** Email delivery. Simulated here; a real one would talk to SMTP or an email API. */
public final class EmailNotificationSender implements NotificationSender {

    private final String fromAddress;
    private final PrintStream transport;

    public EmailNotificationSender(String fromAddress, PrintStream transport) {
        this.fromAddress = Objects.requireNonNull(fromAddress, "fromAddress");
        this.transport = Objects.requireNonNull(transport, "transport");
    }

    @Override
    public String channel() {
        return "email";
    }

    @Override
    public void send(Notification notification) {
        transport.printf("[email] From: %s%n        To: %s%n        Subject: %s%n%n        %s%n",
                fromAddress, notification.recipient(), notification.subject(), notification.body());
    }
}
