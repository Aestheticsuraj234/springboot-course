package com.example.shopease.notification.channel;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/** Picks the sender for a configured channel name. New channels need no change here. */
public final class NotificationSenderRegistry {

    private final Map<String, NotificationSender> byChannel = new TreeMap<>();

    public NotificationSenderRegistry(Collection<? extends NotificationSender> senders) {
        for (NotificationSender sender : senders) {
            if (byChannel.putIfAbsent(sender.channel(), sender) != null) {
                throw new IllegalArgumentException("Two senders registered for channel: " + sender.channel());
            }
        }
    }

    public NotificationSender senderFor(String channel) {
        NotificationSender sender = byChannel.get(channel);
        if (sender == null) {
            throw new IllegalArgumentException(
                    "No sender for channel '" + channel + "'. Available: " + String.join(", ", byChannel.keySet()));
        }
        return sender;
    }
}
