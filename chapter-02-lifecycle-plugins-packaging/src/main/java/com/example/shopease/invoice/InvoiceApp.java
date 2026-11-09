package com.example.shopease.invoice;

import com.example.shopease.invoice.build.BuildInfo;
import com.example.shopease.invoice.format.CsvInvoiceFormatter;
import com.example.shopease.invoice.format.InvoiceFormatters;
import com.example.shopease.invoice.format.TextInvoiceFormatter;
import com.example.shopease.invoice.source.InvoiceSource;
import com.example.shopease.invoice.source.SampleInvoiceSource;
import java.util.List;

/**
 * Composition root and CLI entry point (Main-Class in the jar manifest).
 * Usage: java -jar target/invoice-generator.jar [text|csv]
 */
public final class InvoiceApp {

    private InvoiceApp() {
    }

    public static void main(String[] args) {
        InvoiceFormatters formatters = new InvoiceFormatters(List.of(
                new TextInvoiceFormatter(),
                new CsvInvoiceFormatter()));
        InvoiceSource source = new SampleInvoiceSource();
        String format = args.length > 0 ? args[0] : "text";

        try {
            String rendered = formatters.find(format).format(source.load());
            // Banner on stderr so stdout stays clean for piping, e.g. `... csv > invoice.csv`.
            System.err.println(BuildInfo.load());
            System.out.print(rendered);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }
}
