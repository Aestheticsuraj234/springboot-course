package com.example.shopease.invoice.format;

import com.example.shopease.invoice.domain.Invoice;
import com.example.shopease.invoice.domain.InvoiceLine;
import java.util.StringJoiner;

/** RFC 4180-style CSV, one row per line item - for import into accounting software. */
public final class CsvInvoiceFormatter implements InvoiceFormatter {

    private static final String HEADER = "invoice,customer,date,description,quantity,unit_price,line_total";

    @Override
    public String name() {
        return "csv";
    }

    @Override
    public String format(Invoice invoice) {
        StringJoiner rows = new StringJoiner(System.lineSeparator(), "", System.lineSeparator());
        rows.add(HEADER);
        for (InvoiceLine line : invoice.lines()) {
            rows.add(String.join(",",
                    escape(invoice.number()),
                    escape(invoice.customer()),
                    invoice.issuedOn().toString(),
                    escape(line.description()),
                    Integer.toString(line.quantity()),
                    line.unitPrice().toPlainString(),
                    line.total().toPlainString()));
        }
        return rows.toString();
    }

    /** Quotes a field containing a comma, quote or newline, doubling any embedded quotes. */
    static String escape(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
