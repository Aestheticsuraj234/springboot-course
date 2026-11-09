package com.example.shopease.notification.order;

import com.example.shopease.notification.channel.Notification;
import com.example.shopease.notification.channel.NotificationSender;
import java.util.Objects;

/** Business use case: tell a customer their order is confirmed. Channel-agnostic. */
public final class OrderConfirmationService {

    private final NotificationSender sender;

    public OrderConfirmationService(NotificationSender sender) {
        this.sender = Objects.requireNonNull(sender, "sender");
    }

    public void confirm(String orderId, String customerContact) {
        sender.send(new Notification(
                customerContact,
                "Order " + orderId + " confirmed",
                "Thanks for shopping with ShopEase! Your order " + orderId
                        + " is confirmed and will be dispatched within 24 hours."));
    }
}
