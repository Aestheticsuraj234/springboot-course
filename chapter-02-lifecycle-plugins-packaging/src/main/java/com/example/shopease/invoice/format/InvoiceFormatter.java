package com.example.shopease.invoice.format;

import com.example.shopease.invoice.domain.Invoice;

/** Renders an invoice in one output format. Add a format by adding an implementation. */
public interface InvoiceFormatter {

    /** The name users pass on the command line, e.g. {@code text} or {@code csv}. */
    String name();

    String format(Invoice invoice);
}
