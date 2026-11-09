package com.example.shopease.invoice;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * Integration test run by maven-failsafe-plugin AFTER the package phase.
 * It launches the real jar the way a user would: java -jar invoice-generator.jar csv
 */
class InvoiceJarIT {

    @Test
    void packagedJarIsExecutableAndHasFilteredBuildInfo() throws Exception {
        Path jar = Path.of(System.getProperty("app.jar"));
        assertTrue(Files.isRegularFile(jar), "jar not built: " + jar);

        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process process = new ProcessBuilder(java, "-jar", jar.toString(), "csv")
                .redirectErrorStream(true)
                .start();
        String output = new String(process.getInputStream().readAllBytes(), UTF_8);

        assertTrue(process.waitFor(30, TimeUnit.SECONDS), "app did not exit");
        assertEquals(0, process.exitValue(), output);
        assertTrue(output.contains("invoice,customer,date"), output);
        assertTrue(output.contains("invoice-generator 1.0.0-SNAPSHOT"), output);
        assertFalse(output.contains("${"), "resource filtering did not run: " + output);
    }
}
