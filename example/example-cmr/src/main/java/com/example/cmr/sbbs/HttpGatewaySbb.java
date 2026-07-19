/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.sbbs;

import com.example.cmr.http.HttpReply;
import com.example.cmr.http.MonitorHandler;
import com.example.cmr.http.SiteHandler;

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
 * The single HTTP gateway SBB for the whole CMR app. {@code ra-http-server}
 * fires one event type ({@link HttpWebRequestEvent}) for every request, and
 * {@code mapEventToSbb} keys on the event class, so exactly one SBB must own
 * ingress and dispatch by path:
 *
 * <ol>
 *   <li>{@link MonitorHandler} first — the observability surface
 *       ({@code /telemetry}, {@code /api/telemetry/*}, {@code /api/autonomous/*},
 *       {@code /api/ai/*}); it returns empty for paths it doesn't own;</li>
 *   <li>{@link SiteHandler} otherwise — the CMR admin + public site.</li>
 * </ol>
 *
 * <p>The computed {@link HttpReply} is written back to the pending request
 * through the injected {@code ra-http-server} command port as an
 * {@code HttpResponseExCommand} (headers + text/binary body). No Vert.x, no
 * second HTTP server: the entire app lives behind the RA contract.</p>
 */
public final class HttpGatewaySbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HttpGatewaySbb.class);

    private final MonitorHandler monitor;   // nullable — telemetry may be disabled
    private final SiteHandler site;

    /** Injected by the container at activation; matches {@code RaEndpointPort.getRaName()}. */
    @InjectRa(name = "http-server-ra")
    private volatile RaCommandPort http;

    private volatile SbbLocalObject self;

    public HttpGatewaySbb(MonitorHandler monitor, SiteHandler site) {
        this.monitor = monitor;
        this.site = site;
    }

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

    @Override
    public void sbbCreate() {
    }

    @Override
    public void sbbActivate() {
    }

    @Override
    public void sbbPassivate() {
    }

    @Override
    public void sbbRemove() {
    }

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
            return monitor.handle(req).orElseGet(() -> site.handle(req));
        }
        return site.handle(req);
    }
}
