package com.example.shopease.pricing.policy;

import com.example.shopease.pricing.domain.Money;

/** Computes the tax due on an amount that has already been discounted. */
@FunctionalInterface
public interface TaxPolicy {

    Money taxOn(Money taxableAmount);
}
