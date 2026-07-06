package com.microjainslee.telemetry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microjainslee.core.MicroSleeContainer;

/**
 * Full-featured TelemetryPort implementation backed by Micrometer.
 * Passive collection only — callbacks from EventRouter/RA endpoints.
 * Single daemon VT for periodic evaluation (ResourceMonitor + AutoReconfigEngine).
 */
public final class MicrometerTelemetryPort implements TelemetryPort {

    private static final Logger LOG = LogManager.getLogger(MicrometerTelemetryPort.class);

    private final SbbCollector sbbCollector;
    private final RaCollector raCollector;
    private final ErrorCollector errorCollector;
    private final ResourceMonitor resourceMonitor;
    private final SpunkDetector spunkDetector;
    private final StaleDetector staleDetector;
    private final AlarmEngine alarmEngine;
    private final AutoReconfigEngine autoReconfig;
    private final PrometheusExporter prometheusExporter;
    private final MicroSleeContainer container;

    private final AtomicBoolean autoReconfigEnabled = new AtomicBoolean(true);
