package com.example.shopease.invoice.format;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceFormattersTest {

    private final InvoiceFormatters formatters =
            new InvoiceFormatters(List.of(new TextInvoiceFormatter(), new CsvInvoiceFormatter()));

    @Test
    void findsFormatterByName() {
        assertInstanceOf(CsvInvoiceFormatter.class, formatters.find("csv"));
    }

    @Test
    void unknownFormatListsTheAvailableOnes() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> formatters.find("pdf"));
        assertTrue(e.getMessage().contains("csv, text"), e.getMessage());
    }

    @Test
    void rejectsDuplicateNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new InvoiceFormatters(List.of(new CsvInvoiceFormatter(), new CsvInvoiceFormatter())));
    }
}
