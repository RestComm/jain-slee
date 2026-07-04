/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.quarkus;

import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.TimerPort;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Quarkus build-time augmentation recorder for the embedded micro JAIN-SLEE container.
 *
 * <p>All methods on this class are invoked by Quarkus during the static-init / runtime-init
 * phases. Container + facility instances are stored in static fields that the
 * {@link MicroJainsleeProducer} reads at runtime.</p>
 */
@Recorder
public class MicroJainsleeRecorder {

    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger(MicroJainsleeRecorder.class);

    private static volatile MicroSleeContainer container;
    private static volatile EventRouter eventRouter;
    private static volatile TimerPort timerPort;
    private static volatile com.microjainslee.core.MicroSleeContainer.AcnfBackend acnf;

    /**
     * Build a fresh {@link MicroSleeContainer} using the supplied configuration and stash
     * it in the static holder for the runtime CDI producer. Called at static-init.
     *
     * @param config immutable micro-container configuration resolved at build time
     * @return runtime handle to the new container
     */
    public RuntimeValue<MicroSleeContainer> createContainer(MicroSleeConfiguration config) {
        if (config == null) {
            config = MicroSleeConfiguration.defaults();
        }
        MicroSleeContainer c = new MicroSleeContainer(config);
        container = c;
        eventRouter = c.getEventRouter();
        timerPort = c.getTimerPort();
        acnf = c.getActivityContextNamingFacility();
        LOG.info("MicroSleeContainer constructed: bufferSize={}, preferVT={}, sbbPool={}-{}, perVT={}",
                config.getEventRouterBufferSize(), config.isPreferVirtualThreads(),
                config.getSbbPoolMin(), config.getSbbPoolMax(), config.isSbbPerVirtualThread());
        return new RuntimeValue<MicroSleeContainer>(c);
    }

    /** Start the previously-created container. Idempotent. Called at runtime-init. */
    public void startContainer() {
        if (container != null) {
            LOG.info("Starting MicroSleeContainer (state={})", container.getState());
            container.start();
            LOG.info("MicroSleeContainer started (state={})", container.getState());
        } else {
            LOG.warn("startContainer() called but container is null");
        }
    }

    /** Stop the previously-started container. Called from the Quarkus shutdown hook. */
    public void stopContainer() {
        if (container != null) {
            LOG.info("Stopping MicroSleeContainer (state={})", container.getState());
            container.stop();
            LOG.info("MicroSleeContainer stopped");
        }
    }

    /**
     * Register pooled SBB types discovered at build time. Uses no-arg constructor
     * for each class; applications with CDI-managed SBBs should also call
     * {@code registerSbbType} manually with an Arc supplier if needed.
     */
    public void registerSbbTypes(java.util.List<String> classNames) {
        if (container == null || classNames == null || classNames.isEmpty()) {
            return;
        }
        for (String fqn : classNames) {
            try {
                Class<?> clazz = Class.forName(fqn);
                if (!com.microjainslee.api.Sbb.class.isAssignableFrom(clazz)) {
                    LOG.warn("Skipping non-Sbb type {}", fqn);
                    continue;
                }
                @SuppressWarnings("unchecked")
                Class<? extends com.microjainslee.api.Sbb> sbbClass =
                        (Class<? extends com.microjainslee.api.Sbb>) clazz;
                container.registerSbbType(sbbClass, new java.util.function.Supplier<com.microjainslee.api.Sbb>() {
                    @Override
                    public com.microjainslee.api.Sbb get() {
                        try {
                            return sbbClass.getDeclaredConstructor().newInstance();
                        } catch (ReflectiveOperationException e) {
                            throw new IllegalStateException("Cannot instantiate SBB " + fqn, e);
                        }
                    }
                });
                LOG.info("Registered pooled SBB type {}", fqn);
            } catch (ClassNotFoundException e) {
                LOG.warn("Failed to load SBB class {}: {}", fqn, e.getMessage());
            }
        }
    }

    public RuntimeValue<MicroSleeContainer> containerRuntimeValue(MicroSleeConfiguration cfg) {
        return new RuntimeValue<MicroSleeContainer>(container);
    }

    public RuntimeValue<EventRouter> eventRouterRuntimeValue(MicroSleeConfiguration cfg) {
        return new RuntimeValue<EventRouter>(eventRouter);
    }

    public RuntimeValue<TimerPort> timerPortRuntimeValue(MicroSleeConfiguration cfg) {
        return new RuntimeValue<TimerPort>(timerPort);
    }

    public RuntimeValue<com.microjainslee.core.MicroSleeContainer.AcnfBackend> acnfRuntimeValue(MicroSleeConfiguration cfg) {
        return new RuntimeValue<com.microjainslee.core.MicroSleeContainer.AcnfBackend>(acnf);
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 2 — 3-port local RA registration (recorder)
    // ──────────────────────────────────────────────────────────

    /**
     * GOAL 2 — register a local Resource Adaptor via the 3-port contract.
     * <p>
     * The RA is registered with the container's {@link MicroSleeContainer#registerRa}
     * and activated if the container is already started.
     *
     * @param name     the RA entity name (must match {@link RaEndpointPort#getRaName()})
     * @param endpoint the RA endpoint port (lifecycle owner)
     * @param command  the RA command port (SBB-to-RA outbound commands)
     */
    public void registerRa(String name, RaEndpointPort endpoint, RaCommandPort command) {
        if (container == null) {
            LOG.warn("registerRa() called but container is null (name={})", name);
            return;
        }
        if (endpoint == null || command == null) {
            LOG.warn("registerRa() called with null endpoint or command (name={})", name);
            return;
        }
        container.registerRa(endpoint, command);
    }

    /**
     * GOAL 2 — register a RA from a single class that implements both
     * {@link RaEndpointPort} and {@link RaCommandPort}.
     * <p>
     * The class is loaded and instantiated via no-arg constructor at
     * {@code RUNTIME_INIT}. This is the safe variant for build-time
     * discovery: the Processor passes class names through to the
     * recorder instead of serialising arbitrary object instances.
     *
     * @param className fully-qualified class name of a class implementing
     *                  both {@code RaEndpointPort} and {@code RaCommandPort}
     */
    public void registerRaFromClassName(String className) {
        if (container == null) {
            LOG.warn("registerRaFromClassName() called but container is null (class={})", className);
            return;
        }
        if (className == null || className.trim().isEmpty()) {
            LOG.warn("registerRaFromClassName() called with empty class name");
            return;
        }
        try {
            Class<?> clazz = Class.forName(className, true,
                    Thread.currentThread().getContextClassLoader());
            if (!RaEndpointPort.class.isAssignableFrom(clazz)) {
                LOG.warn("Class {} does not implement RaEndpointPort — skipping", className);
                return;
            }
            if (!RaCommandPort.class.isAssignableFrom(clazz)) {
                LOG.warn("Class {} does not implement RaCommandPort — skipping", className);
                return;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            RaEndpointPort endpoint = (RaEndpointPort) instance;
            RaCommandPort command = (RaCommandPort) instance;
            container.registerRa(endpoint, command);
            LOG.info("Registered RA from class name: {} (class={})",
                    endpoint.getRaName(), className);
        } catch (ReflectiveOperationException e) {
            LOG.warn("Failed to instantiate RA class {}: {}", className, e.getMessage());
        }
    }

    /**
     * GOAL 5 — map an event type to an SBB entity name for convergent routing.
     * <p>
     * Resolves the event class by name at runtime and delegates to
     * {@link MicroSleeContainer#mapEventToSbb(Class, String)}.
     *
     * @param eventClass fully-qualified class name of a {@link SleeEvent} implementation
     * @param sbbName    SBB entity name that handles this event type
     */
    @SuppressWarnings("unchecked")
    public void mapEventToSbb(String eventClass, String sbbName) {
        if (container == null) {
            LOG.warn("mapEventToSbb() called but container is null (event={}, sbb={})", eventClass, sbbName);
            return;
        }
        if (eventClass == null || eventClass.trim().isEmpty()) {
            LOG.warn("mapEventToSbb() called with empty event class name (sbb={})", sbbName);
            return;
        }
        if (sbbName == null || sbbName.trim().isEmpty()) {
            LOG.warn("mapEventToSbb() called with empty SBB name (event={})", eventClass);
            return;
        }
        try {
            Class<?> rawClass = Class.forName(eventClass, true,
                    Thread.currentThread().getContextClassLoader());
            if (!SleeEvent.class.isAssignableFrom(rawClass)) {
                LOG.warn("Class {} is not a SleeEvent — skipping mapping to SBB {}", eventClass, sbbName);
                return;
            }
            Class<? extends SleeEvent> eventType = (Class<? extends SleeEvent>) rawClass;
            container.mapEventToSbb(eventType, sbbName);
        } catch (ClassNotFoundException cnfe) {
            LOG.warn("Event class not found on classpath: {} (sbb={}) — skipping mapping",
                    eventClass, sbbName);
        }
    }
}