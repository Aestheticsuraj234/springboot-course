package com.example.shopease.invoice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void totalsAllLines() {
        Invoice invoice = new Invoice("INV-1", "Customer", LocalDate.of(2026, 1, 1), List.of(
                new InvoiceLine("A", 2, new BigDecimal("100.25")),
                new InvoiceLine("B", 1, new BigDecimal("50"))));

        assertEquals(new BigDecimal("250.50"), invoice.total());
    }

    @Test
    void rejectsEmptyInvoice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice("INV-1", "Customer", LocalDate.of(2026, 1, 1), List.of()));
    }
}
