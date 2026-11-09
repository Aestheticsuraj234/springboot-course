package com.example.shopease.invoice.source;

import com.example.shopease.invoice.domain.Invoice;
import com.example.shopease.invoice.domain.InvoiceLine;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** A fixed demo invoice, standing in for a real billing database. */
public final class SampleInvoiceSource implements InvoiceSource {

    @Override
    public Invoice load() {
        return new Invoice("INV-2026-0042", "Sharma Electronics, Pune", LocalDate.of(2026, 11, 9), List.of(
                new InvoiceLine("27\" 4K Monitor", 2, new BigDecimal("18999")),
                new InvoiceLine("USB-C Docking Station", 1, new BigDecimal("6499.50")),
                new InvoiceLine("HDMI 2.1 Cable (2m)", 4, new BigDecimal("749"))));
    }
}
