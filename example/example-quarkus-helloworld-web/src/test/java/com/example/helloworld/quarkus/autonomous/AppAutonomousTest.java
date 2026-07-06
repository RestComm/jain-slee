package com.example.helloworld.quarkus.autonomous;

import com.example.helloworld.quarkus.autonomous.HealthEvaluator.Status;
import com.example.helloworld.quarkus.support.TelemetryFixtures;
import com.example.helloworld.quarkus.support.TelemetryFixtures.FakeTelemetryPort;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Wiring-level coverage for the {@code autonomous/} module against a real
 * container: {@code install()} arms guardian + health evaluator without error,
 * the health verdict is queryable, and {@code close()} tears everything down
 * cleanly (and is safe to call twice).
 */
class AppAutonomousTest {

    private MicroSleeContainer container;
    private AppAutonomous autonomous;

    @BeforeEach
    void setUp() {
        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(16)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        autonomous = new AppAutonomous();
    }

    @AfterEach
    void tearDown() {
        if (autonomous != null) {
            autonomous.close();
        }
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }

    @Test
    void installArmsGuardianAndHealthEvaluator() {
        var telemetry = new FakeTelemetryPort(TelemetryFixtures.snapshot(20, 0.10, 0, 0, 0));
        assertDoesNotThrow(() -> autonomous.install(container, telemetry));

        assertNotNull(autonomous.health(), "health evaluator must be wired");
        assertEquals(Status.GREEN, autonomous.health().evaluate().status(),
                "a nominal snapshot scores GREEN");
    }

    @Test
    void closeIsCleanAndIdempotent() {
        var telemetry = new FakeTelemetryPort(TelemetryFixtures.snapshot(20, 0.10, 0, 0, 0));
        autonomous.install(container, telemetry);
        assertDoesNotThrow(autonomous::close);
        assertDoesNotThrow(autonomous::close); // second close must not throw
        autonomous = null; // already closed
    }
}
