/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.springboot;

import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.SmartLifecycle;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MicroJainsleeLifecycle implements SmartLifecycle {

    private static final Logger LOG = LogManager.getLogger(MicroJainsleeLifecycle.class);

    private final MicroSleeContainer container;
    private final MicroJainsleeProperties props;
    private final List<RaEndpointPort> raEndpoints;
    private final List<RaCommandPort> raCommands;
    private volatile boolean running = false;

    /**
     * Backward-compatible constructor (no RA ports, no properties).
     * Used by tests and embedders that wire the lifecycle by hand.
     */
    public MicroJainsleeLifecycle(MicroSleeContainer container) {
        this(container, null, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Full constructor accepting properties and RA port lists.
     * Called by {@link MicroJainsleeAutoConfiguration}.
     */
    public MicroJainsleeLifecycle(MicroSleeContainer container,
                                   MicroJainsleeProperties props,
                                   List<RaEndpointPort> raEndpoints,
                                   List<RaCommandPort> raCommands) {
        this.container = container;
        this.props = props;
        this.raEndpoints = raEndpoints != null ? raEndpoints : Collections.emptyList();
        this.raCommands = raCommands != null ? raCommands : Collections.emptyList();
        LOG.debug("MicroJainsleeLifecycle created (phase={}, container={}, raEndpoints={}, raCommands={})",
                Integer.MIN_VALUE + 100,
                container != null ? container.getState() : "null",
                this.raEndpoints.size(), this.raCommands.size());
    }

    @Override public boolean isAutoStartup() { return true; }

    @Override
    public void start() {
        if (container == null) {
            LOG.warn("SmartLifecycle.start() called but no container wired; skipping");
            return;
        }
        LOG.info("SmartLifecycle.start() - starting MicroSleeContainer (current state={})", container.getState());
        container.start();
        running = true;
        LOG.info("MicroSleeContainer started via SmartLifecycle (phase={})", getPhase());

        // GOAL 2 — register any RA endpoint+command pairs from the application context.
        registerResourceAdaptors();

        // GOAL 5 — wire event-to-SBB mappings from configuration properties.
        mapEventsToSbbs();
    }

    @Override
    public void stop() {
        if (container == null) {
            LOG.warn("SmartLifecycle.stop() called but no container wired; skipping");
            running = false;
            return;
        }
        LOG.info("SmartLifecycle.stop() - stopping MicroSleeContainer (current state={})", container.getState());
        try {
            container.stop();
            running = false;
            LOG.info("MicroSleeContainer stopped via SmartLifecycle");
        } catch (Throwable t) {
            LOG.error("MicroSleeContainer.stop() failed: {}", t.getMessage(), t);
            throw t;
        }
    }

    @Override public boolean isRunning() { return running; }
    @Override public int getPhase() { return Integer.MIN_VALUE + 100; }

    // ──────────────────────────────────────────────────────────
    // GOAL 2 — register RA endpoint+command pairs
    // ──────────────────────────────────────────────────────────

    private void registerResourceAdaptors() {
        if (raEndpoints.isEmpty()) {
            LOG.debug("No RaEndpointPort beans found in context; skipping RA registration");
            return;
        }
        Iterator<RaEndpointPort> epIter = raEndpoints.iterator();
        Iterator<RaCommandPort> cmdIter = raCommands.iterator();
        while (epIter.hasNext()) {
            RaEndpointPort endpoint = epIter.next();
            RaCommandPort command = cmdIter.hasNext() ? cmdIter.next() : null;
            if (command == null) {
                LOG.warn("No RaCommandPort available for endpoint '{}' — skipping registration",
                        endpoint.getRaName());
                continue;
            }
            try {
                container.registerRa(endpoint, command);
                LOG.info("Registered RA via lifecycle: {} (command={})",
                        endpoint.getRaName(), command.getClass().getSimpleName());
            } catch (RuntimeException re) {
                LOG.error("Failed to register RA [{}]: {}", endpoint.getRaName(), re.getMessage(), re);
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 5 — event-to-SBB mappings from configuration
    // ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void mapEventsToSbbs() {
        if (props == null || props.getEventToSbbMappings() == null || props.getEventToSbbMappings().isEmpty()) {
            LOG.debug("No event-to-sbb-mappings configured; skipping");
            return;
        }
        for (Map.Entry<String, String> entry : props.getEventToSbbMappings().entrySet()) {
            String eventClassName = entry.getKey();
            String sbbName = entry.getValue();
            if (eventClassName == null || eventClassName.trim().isEmpty()) {
                LOG.warn("Skipping event-to-sbb mapping with empty event class name (sbb={})", sbbName);
                continue;
            }
            if (sbbName == null || sbbName.trim().isEmpty()) {
                LOG.warn("Skipping event-to-sbb mapping with empty SBB name (event={})", eventClassName);
                continue;
            }
            try {
                Class<?> rawClass = Class.forName(eventClassName, true,
                        Thread.currentThread().getContextClassLoader());
                if (!SleeEvent.class.isAssignableFrom(rawClass)) {
                    LOG.warn("Class {} is not a SleeEvent — skipping mapping to SBB {}", eventClassName, sbbName);
                    continue;
                }
                Class<? extends SleeEvent> eventType = (Class<? extends SleeEvent>) rawClass;
                container.mapEventToSbb(eventType, sbbName);
            } catch (ClassNotFoundException cnfe) {
                LOG.warn("Event class not found on classpath: {} (sbb={}) — skipping mapping",
                        eventClassName, sbbName);
            }
        }
    }
}
