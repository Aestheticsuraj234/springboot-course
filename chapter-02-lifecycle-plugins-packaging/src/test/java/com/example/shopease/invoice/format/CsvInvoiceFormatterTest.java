package com.example.shopease.invoice.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.shopease.invoice.domain.Invoice;
import com.example.shopease.invoice.domain.InvoiceLine;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvInvoiceFormatterTest {

    @Test
    void quotesFieldsContainingCommasAndQuotes() {
        Invoice invoice = new Invoice("INV-7", "Rao & Sons, Chennai", LocalDate.of(2026, 3, 15), List.of(
                new InvoiceLine("24\" Monitor", 1, new BigDecimal("9999"))));

        String[] rows = new CsvInvoiceFormatter().format(invoice).split(System.lineSeparator());

        assertEquals("invoice,customer,date,description,quantity,unit_price,line_total", rows[0]);
        assertEquals("INV-7,\"Rao & Sons, Chennai\",2026-03-15,\"24\"\" Monitor\",1,9999.00,9999.00", rows[1]);
    }
}
