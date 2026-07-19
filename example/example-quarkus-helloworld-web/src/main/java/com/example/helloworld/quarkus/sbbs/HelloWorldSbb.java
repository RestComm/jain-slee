package com.example.helloworld.quarkus.sbbs;

import com.example.helloworld.quarkus.http.HttpReply;
import com.example.helloworld.quarkus.http.MonitorHandler;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.ra.httpserver.command.HttpServerCommand;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The single HTTP gateway SBB for the HelloWorld template. {@code ra-http-server}
 * fires one event type ({@link HttpWebRequestEvent}) for every request, and
 * {@code mapEventToSbb} keys on the event class, so exactly one SBB owns ingress
 * and dispatches by path:
 *
 * <ol>
 *   <li>{@link MonitorHandler} first — the observability surface
 *       ({@code /telemetry}, {@code /api/telemetry/*}); it returns empty for
 *       paths it doesn't own;</li>
 *   <li>otherwise the app's own "Hello World" response.</li>
 * </ol>
 *
 * <p>The computed {@link HttpReply} is written back through the injected
 * {@code ra-http-server} command port as an {@code HttpResponseExCommand}. No
 * Vert.x, no second HTTP server — the whole app lives behind the RA contract.
 * This is the reference template every micro-jainslee app should copy.</p>
 */
public final class HelloWorldSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HelloWorldSbb.class);

    private final MonitorHandler monitor;   // nullable — telemetry may be disabled

    /** Injected by the container at activation; matches {@code RaEndpointPort.getRaName()}. */
    @InjectRa(name = "http-server-ra")
    private volatile RaCommandPort http;

    private volatile SbbLocalObject self;

    public HelloWorldSbb(MonitorHandler monitor) {
        this.monitor = monitor;
    }

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
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
        if (!(event instanceof HttpWebRequestEvent req)) {
            return;
        }
        HttpReply reply;
        try {
            reply = dispatch(req);
        } catch (RuntimeException ex) {
            LOG.error("[gateway] handler failed for {} {}", req.getMethod(), req.getPath(), ex);
            reply = HttpReply.html(500, "Internal error");
        }
        RaCommandPort port = this.http;
        if (port == null) {
            LOG.warn("[gateway] no command port injected — dropping response for {}", req.getPath());
            return;
        }
        port.sendCommand(new HttpServerCommand.HttpResponseExCommand(
                req.getSessionId(), reply.status(), reply.contentType(),
                reply.text(), reply.binary(), reply.headers()));
    }

    private HttpReply dispatch(HttpWebRequestEvent req) {
        if (monitor != null) {
            return monitor.handle(req).orElseGet(() -> hello(req));
        }
        return hello(req);
    }

    private HttpReply hello(HttpWebRequestEvent req) {
        String userAgent = req.getUserAgent() != null ? req.getUserAgent() : "unknown";
        LOG.info("[HelloWorld] {} {} — Hello World {}", req.getMethod(), req.getPath(), userAgent);
        return HttpReply.html("hello team, running on Quarkus - nhân");
    }
}
