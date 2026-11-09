package com.example.shopease.invoice.source;

import com.example.shopease.invoice.domain.Invoice;

/** Where invoices come from: a database, an API, a file... The app doesn't care. */
@FunctionalInterface
public interface InvoiceSource {

    Invoice load();
}
