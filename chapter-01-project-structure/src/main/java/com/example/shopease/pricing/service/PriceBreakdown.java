package com.example.shopease.pricing.service;

import com.example.shopease.pricing.domain.Money;

/** The itemised result shown on a checkout page or invoice. */
public record PriceBreakdown(Money subtotal, Money discount, Money tax, Money total) {
}
