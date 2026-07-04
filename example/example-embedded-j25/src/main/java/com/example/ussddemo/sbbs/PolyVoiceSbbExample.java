/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.sbbs;

import com.example.ussddemo.commands.GrpcMenuCommand;
import com.example.ussddemo.commands.HttpCallbackCommand;
import com.example.ussddemo.embedded.EmbeddedUssdMain;
import com.example.ussddemo.events.GrpcBackendResponseEvent;
import com.example.ussddemo.events.HttpUssdBeginEvent;
import com.example.ussddemo.events.UssdResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.TimerFiredEvent;
import com.microjainslee.api.TimerPort;
import com.microjainslee.api.annotations.InjectRa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PolyVoice 3-Port Contract SBB — demonstrates all three communication
 * ports available to an SBB in the micro-jainslee container.
 *
 * <h3>Port 1 — Event Handler (Inbound)</h3>
 * The SBB receives SLEE events via {@link #onEvent(SleeEvent, ActivityContextInterface)}.
 * This is the primary inbound path: the SBB reacts to protocol events
 * (HTTP begin, gRPC response, timer fire) arriving through the Event Router.
 *
 * <h3>Port 2 — RA Command Port (Outbound)</h3>
 * The SBB sends outbound commands to Resource Adaptors via {@code @InjectRa}-injected
 * {@link RaCommandPort} fields. The container populates these fields at SBB creation
 * time using the RA entity name specified in the annotation.
 *
 * <h3>Port 3 — Timer Facility (Internal)</h3>
 * The SBB schedules and cancels timers via {@link TimerPort}, obtained from
 * {@code MicroSleeContainer.getTimerPort()}. Timer fires arrive as
 * {@link TimerFiredEvent} instances on Port 1.
 *
 * <p>This SBB is an illustrative example only — it is not registered in
 * the embedded bootstrap. Production SBBs (e.g. {@link HttpServerSbb},
 * {@link Ss7UssdIngressSbb}) use the same patterns.</p>
 */
public final class PolyVoiceSbbExample implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(PolyVoiceSbbExample.class);

    // ─────────────────────────────────────────────────────────────────
    //  Port 1 — the SBB's identity in the SLEE.
    // ─────────────────────────────────────────────────────────────────

    private volatile SbbLocalObject self;

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

    // ─────────────────────────────────────────────────────────────────
    //  Port 2 — injected RA command ports (GOAL 1-5 @InjectRa).
    // ─────────────────────────────────────────────────────────────────

    /** Outbound port to the gRPC menu RA. */
    @InjectRa(name = "grpcMenuRa")
    private volatile RaCommandPort grpcCommandPort;

    /** Outbound port to the HTTP ingress RA. */
    @InjectRa(name = "httpIngressRa")
    private volatile RaCommandPort httpCommandPort;

    // ─────────────────────────────────────────────────────────────────
    //  Port 3 — timer state.
    // ─────────────────────────────────────────────────────────────────

    private static final long DEFAULT_TIMEOUT_MS = 25_000L;
    private volatile long activeTimerId = -1L;

    // ─────────────────────────────────────────────────────────────────
    //  Lifecycle (all 3 ports share the same lifecycle).
    // ─────────────────────────────────────────────────────────────────

    @Override
    public void sbbCreate() {
        LOG.debug("PolyVoiceSbbExample created");
    }

    @Override
    public void sbbActivate() {
        LOG.debug("PolyVoiceSbbExample activated — Port 2 fields injected: grpc={}, http={}",
                grpcCommandPort != null, httpCommandPort != null);
    }

    @Override
    public void sbbPassivate() {
        cancelActiveTimer();
    }

    @Override
    public void sbbRemove() {
        cancelActiveTimer();
    }

    // ─────────────────────────────────────────────────────────────────
    //  Port 1 — Event Handler (inbound events via EventRouter).
    // ─────────────────────────────────────────────────────────────────

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof HttpUssdBeginEvent) {
            onHttpBegin((HttpUssdBeginEvent) event, aci);
        } else if (event instanceof GrpcBackendResponseEvent) {
            onGrpcResponse((GrpcBackendResponseEvent) event, aci);
        } else if (event instanceof TimerFiredEvent) {
            onTimer((TimerFiredEvent) event, aci);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  Port 1 handlers.
    // ─────────────────────────────────────────────────────────────────

    private void onHttpBegin(HttpUssdBeginEvent event, ActivityContextInterface aci) {
        LOG.info("[PolyVoice] HTTP begin session={} msisdn={} text={}",
                event.getSessionId(), event.getMsisdn(), event.getUssdString());

        // Port 3 — arm a session timeout timer.
        TimerPort timer = EmbeddedUssdMain.container().getTimerPort();
        activeTimerId = timer.setTimer(DEFAULT_TIMEOUT_MS, self);

        // Port 2 — send the menu request through the injected gRPC port.
        sendMenuRequest("USSD:" + event.getUssdString());
    }

    private void onGrpcResponse(GrpcBackendResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[PolyVoice] gRPC response session={} text={}",
                event.getSessionId(), event.getMenuText());

        // Port 3 — cancel the session timer since we got a response.
        cancelActiveTimer();

        // Route the final USSD response onward.
        EmbeddedUssdMain.container().routeEvent(
                new UssdResponseEvent(event.getSessionId(), event.getMenuText()), aci);
    }

    private void onTimer(TimerFiredEvent event, ActivityContextInterface aci) {
        if (event.getSbbLocalObject() != self) {
            return; // not our timer
        }
        LOG.warn("[PolyVoice] Timer fired timerId={} — session timeout", event.getTimerId());
        activeTimerId = -1L;
    }

    // ─────────────────────────────────────────────────────────────────
    //  Port 2 — Outbound RA commands via injected ports.
    // ─────────────────────────────────────────────────────────────────

    public void sendMenuRequest(String menuRequest) {
        RaCommandPort port = this.grpcCommandPort;
        if (port == null) {
            LOG.warn("[PolyVoice] grpcCommandPort not injected — command dropped");
            return;
        }
        port.sendCommand(new GrpcMenuCommand(menuRequest));
        LOG.debug("[PolyVoice] gRPC menu command sent: {}", menuRequest);
    }

    public void publishCallback(String sessionId, String responseText, String callbackUrl) {
        RaCommandPort port = this.httpCommandPort;
        if (port == null) {
            LOG.warn("[PolyVoice] httpCommandPort not injected — callback dropped");
            return;
        }
        port.sendCommand(new HttpCallbackCommand(sessionId, responseText, callbackUrl));
        LOG.debug("[PolyVoice] HTTP callback command queued for session={}", sessionId);
    }

    // ─────────────────────────────────────────────────────────────────
    //  Port 3 — Timer helpers.
    // ─────────────────────────────────────────────────────────────────

    public long scheduleTimeout(long timeoutMs) {
        TimerPort timer = EmbeddedUssdMain.container().getTimerPort();
        long id = timer.setTimer(timeoutMs, self);
        LOG.debug("[PolyVoice] Timer set id={} timeoutMs={}", id, timeoutMs);
        return id;
    }

    public void cancelTimer(long timerId) {
        EmbeddedUssdMain.container().getTimerPort().cancelTimer(timerId);
        LOG.debug("[PolyVoice] Timer cancelled id={}", timerId);
    }

    private void cancelActiveTimer() {
        long id = this.activeTimerId;
        if (id >= 0L) {
            cancelTimer(id);
            activeTimerId = -1L;
        }
    }
}