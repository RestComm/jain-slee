/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.jss7.collab.CapProtocolAdapter;
import com.microjainslee.ra.jss7.collab.MapProtocolAdapter;
import com.microjainslee.ra.jss7.collab.Ss7EventPublisher;
import com.microjainslee.ra.jss7.collab.Ss7ProtocolAdapter;
import com.microjainslee.ra.jss7.collab.Ss7TcapListener;
import com.microjainslee.ra.jss7.command.Ss7Command;
import com.microjainslee.ra.jss7.event.Ss7Event;
import com.microjainslee.ra.jss7.transport.Ss7Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * jSS7 Resource Adaptor — owns the full jSS7 stack (SCTP → M3UA → SCCP →
 * TCAP → MAP/CAP) and bridges it to the SLEE event bus.
 *
 * <p>Inbound: protocol adapters ({@link Ss7TcapListener}, {@link MapProtocolAdapter},
 * {@link CapProtocolAdapter}) register listeners against the stack and publish
 * typed events through {@link #publish(String, SleeEvent)}. Outbound: SBB
 * commands are routed to the adapters, then to the TCAP dialog layer.</p>
 *
 * <p>Idle dialog activities are swept on a virtual-thread scheduler.</p>
 */
public final class Ss7ResourceAdaptor implements AutoCloseable, Ss7EventPublisher {

    private static final Logger LOG = LogManager.getLogger(Ss7ResourceAdaptor.class);

    private volatile RaBootstrapPort bootstrap;
    private volatile Ss7RaConfig config = new Ss7RaConfig();
    private volatile Ss7Stack stack;

    private final List<Ss7ProtocolAdapter> adapters = new ArrayList<>();
    private final Map<String, MutableSession> sessions = new ConcurrentHashMap<>();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final IdleSweeper sweeper = new IdleSweeper();
    private int idleTimeoutSeconds = 300;

    // ── configuration ────────────────────────────────────────
    public void setBootstrapPort(RaBootstrapPort bp) { this.bootstrap = bp; }
    public RaBootstrapPort bootstrap() { return bootstrap; }
    public void setConfig(Ss7RaConfig cfg) { this.config = cfg; }
    public Ss7RaConfig config() { return config; }
    public void setIdleTimeoutSeconds(int s) { this.idleTimeoutSeconds = s; }
    public Ss7Stack stack() { return stack; }

    // ── lifecycle ────────────────────────────────────────────
    public void raActive() {
        if (!active.compareAndSet(false, true)) return;
        try {
            this.stack = new Ss7Stack(config);
            this.stack.start();

            adapters.clear();
            adapters.add(new Ss7TcapListener());
            if (config.mapEnabled()) adapters.add(new MapProtocolAdapter());
            if (config.capEnabled()) adapters.add(new CapProtocolAdapter());
            for (Ss7ProtocolAdapter a : adapters) {
                a.attach(stack, this);
            }

            sweeper.start(idleTimeoutSeconds);
            LOG.info("jSS7 RA activated (adapters={}, idleTimeout={}s)", adapters.size(), idleTimeoutSeconds);
        } catch (Exception e) {
            active.set(false);
            LOG.error("jSS7 RA activation failed", e);
            throw new IllegalStateException("jSS7 RA activation failed", e);
        }
    }

    public void raInactive() {
        if (!active.compareAndSet(true, false)) return;
        sweeper.stop();
        for (Ss7ProtocolAdapter a : adapters) {
            try { a.detach(); } catch (RuntimeException e) { LOG.warn("detach {} failed", a.protocol(), e); }
        }
        adapters.clear();
        if (stack != null) { stack.stop(); stack = null; }
        sessions.values().forEach(s -> endActivity(s));
        sessions.clear();
        LOG.info("jSS7 RA deactivated");
    }

    public boolean isActive() { return active.get(); }

    // ── inbound: jSS7 → SLEE (Ss7EventPublisher) ─────────────
    @Override
    public void publish(String dialogId, SleeEvent event) {
        if (!active.get() || bootstrap == null) {
            LOG.warn("RA not active — dropping {} on {}", event.getClass().getSimpleName(), dialogId);
            return;
        }
        MutableSession s = sessions.computeIfAbsent(dialogId,
                id -> new MutableSession(id, bootstrap.createActivityHandle(id)));
        s.touch();
        bootstrap.fireEvent(event, s.activityHandle, null);
        LOG.debug("Fired {} on dialog={}", event.getClass().getSimpleName(), dialogId);

        // end the activity when the dialog terminates
        if (event instanceof Ss7Event.TcapEnd || event instanceof Ss7Event.TcapAbort) {
            forceEndSession(dialogId, sessions.get(dialogId));
        }
    }

    /** Back-compat convenience for the generic TCAP path. */
    public void fireEventOnDialog(String dialogId, Ss7Event event) {
        publish(dialogId, event);
    }

    // ── outbound: SBB → jSS7 ─────────────────────────────────
    public void sendOutbound(Ss7Command cmd) {
        if (!active.get()) {
            LOG.warn("RA not active — dropping {}", cmd.getClass().getSimpleName());
            return;
        }
        // protocol adapters get first refusal (MAP/CAP typed commands)
        for (Ss7ProtocolAdapter a : adapters) {
            if (a.sendOutbound(cmd)) {
                touch(cmd.dialogId());
                return;
            }
        }
        // fall back to generic TCAP dialog handling
        switch (cmd) {
            case Ss7Command.TcapBegin b    -> logCmd("BEGIN", b);
            case Ss7Command.TcapContinue c -> logCmd("CONTINUE", c);
            case Ss7Command.TcapEnd e      -> { logCmd("END", e); forceEndSession(e.dialogId(), sessions.get(e.dialogId())); }
            case Ss7Command.TcapAbort a    -> { logCmd("ABORT", a); forceEndSession(a.dialogId(), sessions.get(a.dialogId())); }
            case Ss7Command.TcapUni u      -> logCmd("UNI", u);
        }
        touch(cmd.dialogId());
    }

    // ── session management ────────────────────────────────────
    public void forceEndSession(String did, MutableSession s) {
        if (s == null) return;
        sessions.remove(did);
        endActivity(s);
        LOG.debug("Ended session {}", did);
    }

    private void endActivity(MutableSession s) {
        if (bootstrap != null && s.activityHandle != null) {
            bootstrap.endActivity(s.activityHandle);
        }
    }

    private void touch(String did) {
        MutableSession s = sessions.get(did);
        if (s != null) s.touch();
    }

    private void sweepIdle() {
        long cutoff = System.currentTimeMillis() - (idleTimeoutSeconds * 1000L);
        sessions.entrySet().removeIf(e -> {
            if (e.getValue().lastActivity < cutoff) {
                forceEndSession(e.getKey(), e.getValue());
                return true;
            }
            return false;
        });
    }

    private void logCmd(String label, Ss7Command cmd) {
        LOG.info("TCAP {}: did={} target={}", label, cmd.dialogId(), cmd.targetAddress());
    }

    @Override public void close() { raInactive(); }

    // ── idle sweeper (Java 21+ virtual-thread scheduler) ──────
    private final class IdleSweeper {
        private volatile Thread thread;
        private volatile boolean running;

        void start(int idleSeconds) {
            running = true;
            long periodMs = Math.max(1, idleSeconds / 2) * 1000L;
            thread = Thread.ofVirtual().name("ra-jss7-sweeper").start(() -> {
                while (running) {
                    try {
                        Thread.sleep(periodMs);
                        sweepIdle();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    } catch (RuntimeException e) {
                        LOG.warn("idle sweep failed", e);
                    }
                }
            });
        }

        void stop() {
            running = false;
            Thread t = thread;
            if (t != null) t.interrupt();
        }
    }

    // ── per-dialog session ────────────────────────────────────
    public static final class MutableSession {
        final String sessionId;
        final ActivityHandle activityHandle;
        final long createdAt;
        volatile long lastActivity;
        MutableSession(String sid, ActivityHandle h) {
            this.sessionId = sid; this.activityHandle = h;
            this.createdAt = System.currentTimeMillis(); this.lastActivity = this.createdAt;
        }
        void touch() { this.lastActivity = System.currentTimeMillis(); }
    }
}
