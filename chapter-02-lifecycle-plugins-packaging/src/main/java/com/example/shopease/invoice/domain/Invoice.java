package com.example.shopease.invoice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/** An issued invoice. Pure data plus its own total - rendering lives elsewhere. */
public record Invoice(String number, String customer, LocalDate issuedOn, List<InvoiceLine> lines) {

    public Invoice {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("invoice number is required");
        }
        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("customer is required");
        }
        Objects.requireNonNull(issuedOn, "issuedOn");
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("an invoice needs at least one line");
        }
        lines = List.copyOf(lines);
    }

    public BigDecimal total() {
        return lines.stream().map(InvoiceLine::total).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }
}
