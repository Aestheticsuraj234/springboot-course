package com.example.shopease.invoice.format;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/** Looks up a formatter by name. Knows nothing about any concrete format. */
public final class InvoiceFormatters {

    private final Map<String, InvoiceFormatter> byName = new TreeMap<>();

    public InvoiceFormatters(Collection<? extends InvoiceFormatter> formatters) {
        for (InvoiceFormatter formatter : formatters) {
            if (byName.putIfAbsent(formatter.name(), formatter) != null) {
                throw new IllegalArgumentException("Duplicate invoice format: " + formatter.name());
            }
        }
    }

    public InvoiceFormatter find(String name) {
        InvoiceFormatter formatter = byName.get(name);
        if (formatter == null) {
            throw new IllegalArgumentException(
                    "Unknown format '" + name + "'. Available: " + String.join(", ", byName.keySet()));
        }
        return formatter;
    }
}
