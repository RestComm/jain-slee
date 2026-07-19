package com.example.helloworld.quarkus.bootstrap;

import com.example.helloworld.quarkus.http.MonitorHandler;
import com.example.helloworld.quarkus.sbbs.HelloWorldSbb;
import com.example.helloworld.quarkus.telemetry.AppTelemetry;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import com.microjainslee.telemetry.TelemetryPort;

import io.quarkus.runtime.StartupEvent;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bootstrap — Quarkus wiring for the HelloWorld template. Optional telemetry
 * sits on top of the core container; the dashboard and telemetry APIs ride the
 * same {@code ra-http-server} as the app response through {@link HelloWorldSbb}.
 *
 * <p>Must observe {@link StartupEvent}: an {@code @ApplicationScoped} bean with
 * only {@code @PostConstruct} is lazy, and nothing else injects this class after
 * autonomous/AI were removed — so the HTTP RA would never register.</p>
 */
@ApplicationScoped
public final class HelloWorldBootstrap {

    private static final Logger LOG = LogManager.getLogger(HelloWorldBootstrap.class);

    @Inject
    MicroSleeContainer container;

    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "http.ra.port", defaultValue = "8081")
    int httpRaPort;

    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "microjainslee.telemetry.enabled", defaultValue = "true")
    boolean telemetryEnabled;

    private volatile HttpServerRaEndpoint httpEndpoint;
    private volatile boolean started;

    private final AppTelemetry appTelemetry = new AppTelemetry();

    void onStart(@Observes StartupEvent ev) {
        if (started) {
            return;
        }
        started = true;
        LOG.info("HelloWorld bootstrap triggered by StartupEvent");

        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }

        TelemetryPort telemetry = null;
        if (telemetryEnabled) {
            telemetry = appTelemetry.install(container);
        } else {
            LOG.info("telemetry module disabled (microjainslee.telemetry.enabled=false)");
        }

        MonitorHandler monitor = telemetry == null ? null : new MonitorHandler(telemetry);

        container.registerSbbType(HelloWorldSbb.class, () -> new HelloWorldSbb(monitor));
        container.createIesDispatcher();
        container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");
        wireHttpRa();

        LOG.info("HelloWorld bootstrap complete. App + dashboard on http://localhost:{} "
                + "(app: /, dashboard: /telemetry)", httpRaPort);
    }

    @PreDestroy
    void shutdown() {
        appTelemetry.close();
        if (httpEndpoint != null) {
            httpEndpoint.deactivate();
        }
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }

    private void wireHttpRa() {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpRaPort);
        ra.setHost("0.0.0.0");

        httpEndpoint = new HttpServerRaEndpoint(ra);
        httpEndpoint.setPort(httpRaPort);

        container.registerRa(httpEndpoint, httpEndpoint);
        LOG.info("ra-http-server registered on port {}", httpRaPort);
    }
}
