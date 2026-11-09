package com.example.shopease.invoice.format;

import com.example.shopease.invoice.domain.Invoice;
import com.example.shopease.invoice.domain.InvoiceLine;

/** Human-readable invoice, suitable for printing or an email body. */
public final class TextInvoiceFormatter implements InvoiceFormatter {

    private static final String ROW = "%-30s %5s %12s %12s%n";
    private static final String RULE = "-".repeat(62) + System.lineSeparator();

    @Override
    public String name() {
        return "text";
    }

    @Override
    public String format(Invoice invoice) {
        StringBuilder out = new StringBuilder()
                .append("INVOICE ").append(invoice.number()).append(System.lineSeparator())
                .append("Customer : ").append(invoice.customer()).append(System.lineSeparator())
                .append("Date     : ").append(invoice.issuedOn()).append(System.lineSeparator())
                .append(RULE)
                .append(String.format(ROW, "Item", "Qty", "Unit (INR)", "Total (INR)"))
                .append(RULE);
        for (InvoiceLine line : invoice.lines()) {
            out.append(String.format(ROW, line.description(), line.quantity(),
                    line.unitPrice().toPlainString(), line.total().toPlainString()));
        }
        return out.append(RULE)
                .append(String.format(ROW, "TOTAL", "", "", invoice.total().toPlainString()))
                .toString();
    }
}
