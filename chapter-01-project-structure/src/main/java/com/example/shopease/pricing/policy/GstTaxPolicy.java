package com.example.shopease.pricing.policy;

import com.example.shopease.pricing.domain.Money;
import java.math.BigDecimal;
import java.util.Objects;

/** Indian Goods and Services Tax at a single flat rate (e.g. 18% for electronics). */
public final class GstTaxPolicy implements TaxPolicy {

    private final BigDecimal ratePercent;

    public GstTaxPolicy(BigDecimal ratePercent) {
        Objects.requireNonNull(ratePercent, "ratePercent");
        if (ratePercent.signum() < 0) {
            throw new IllegalArgumentException("GST rate cannot be negative: " + ratePercent);
        }
        this.ratePercent = ratePercent;
    }

    @Override
    public Money taxOn(Money taxableAmount) {
        return taxableAmount.percentage(ratePercent);
    }
}
