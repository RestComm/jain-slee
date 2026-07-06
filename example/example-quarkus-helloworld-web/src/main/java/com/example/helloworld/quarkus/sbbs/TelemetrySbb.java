package com.example.helloworld.quarkus.sbbs;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.httpserver.command.HttpServerCommand;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import com.microjainslee.telemetry.TelemetryPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SBB that handles telemetry HTTP requests arriving through the ra-http-server pipeline.
 * Delegates to {@link TelemetryPort} for all data; the REST API is also served
 * directly via Vert.x Router in {@code HelloWorldBootstrap}.
 */
public final class TelemetrySbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(TelemetrySbb.class);

    private final TelemetryPort telemetry;

    @InjectRa(name = "http-server-ra")
    private volatile RaCommandPort httpPort;

    public TelemetrySbb(MicroSleeContainer container, TelemetryPort telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void sbbCreate() { }

    @Override
    public void sbbActivate() { }

    @Override
    public void sbbPassivate() { }

    @Override
    public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof HttpWebRequestEvent req
                && req.getPath() != null
                && req.getPath().startsWith("/api/telemetry/")) {
            handleTelemetry(req);
        }
    }

    private void handleTelemetry(HttpWebRequestEvent req) {
        String path = req.getPath();
        LOG.debug("TelemetrySbb handling {}", path);

        // For now, telemetry is served directly via Vert.x / Spring REST.
        // This SBB provides the JAIN SLEE pipeline path for future use.
        // The bootstrap's Vert.x Router handles the actual responses.
    }
}
