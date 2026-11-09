package com.example.shopease.notification.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/** Guards the build itself: whatever profile is active, the packaged config must be fully resolved. */
class PackagedConfigTest {

    @Test
    void packagedApplicationPropertiesAreFullyFiltered() {
        assertDoesNotThrow(() -> AppConfig.from(new ClasspathPropertiesLoader().load("application.properties")));
    }
}
