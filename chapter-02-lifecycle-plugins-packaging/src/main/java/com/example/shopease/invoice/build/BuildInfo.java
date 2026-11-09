package com.example.shopease.invoice.build;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/** Name, version and build time, injected into build-info.properties by Maven resource filtering. */
public record BuildInfo(String name, String version, String builtAt) {

    private static final String RESOURCE = "build-info.properties";

    public static BuildInfo load() {
        try (InputStream in = BuildInfo.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Missing classpath resource: " + RESOURCE);
            }
            Properties props = new Properties();
            props.load(in);
            return new BuildInfo(
                    props.getProperty("app.name"),
                    props.getProperty("app.version"),
                    props.getProperty("app.built-at"));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + RESOURCE, e);
        }
    }

    @Override
    public String toString() {
        return name + " " + version + " (built " + builtAt + ")";
    }
}
