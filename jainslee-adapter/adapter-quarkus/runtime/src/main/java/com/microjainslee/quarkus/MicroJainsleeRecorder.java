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
import com.microjainslee.core.EventDeliveryMode;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.ShutdownContext;
import io.quarkus.runtime.annotations.Recorder;

/**
 * Quarkus build-time augmentation recorder for the embedded micro JAIN-SLEE container.
 *
 * <p>All methods on this class are invoked by Quarkus during the static-init / runtime-init
 * phases. Container + facility instances are stored in static fields that the
 * {@link MicroJainsleeProducer} reads at runtime.</p>
 */
@Recorder
public class MicroJainsleeRecorder {

    private static final org.jboss.logging.Logger LOG = org.jboss.logging.Logger.getLogger(MicroJainsleeRecorder.class);

    private static volatile MicroSleeContainer container;
    private static volatile EventRouter eventRouter;
    private static volatile TimerPort timerPort;
    private static volatile com.microjainslee.core.MicroSleeContainer.AcnfBackend acnf;

    /**
     * Build a fresh {@link MicroSleeContainer} from primitive build-time settings and stash
     * it in the static holder. Primitives (not {@link MicroSleeConfiguration}) are passed so
     * the Quarkus bytecode recorder can serialise the call — the config class has read-only
     * fields that the recorder cannot reconstruct.
     */
    public RuntimeValue<MicroSleeContainer> createContainer(int bufferSize,
                                                            boolean preferVirtualThreads,
                                                            int sbbPoolMin,
                                                            int sbbPoolMax,
                                                            boolean sbbPerVirtualThread,
                                                            int sbbTypePoolMinIdle,
                                                            String eventDelivery,
                                                            boolean offHeapEnabled,
                                                            String offHeapStorageDir) {
        MicroSleeConfiguration config = MicroSleeConfiguration.builder()
                .eventRouterBufferSize(bufferSize)
                .preferVirtualThreads(preferVirtualThreads)
                .sbbPoolMin(sbbPoolMin)
                .sbbPoolMax(sbbPoolMax)
                .sbbPerVirtualThread(sbbPerVirtualThread)
                .sbbTypePoolMinIdle(sbbTypePoolMinIdle)
                .eventDeliveryMode(EventDeliveryMode.parse(eventDelivery))
                .offHeapEnabled(offHeapEnabled)
                .offHeapStorageDir(offHeapStorageDir != null ? offHeapStorageDir : "")
                .build();
        MicroSleeContainer c = new MicroSleeContainer(config);
        container = c;
        eventRouter = c.getEventRouter();
        timerPort = c.getTimerPort();
        acnf = c.getActivityContextNamingFacility();
        LOG.infof("MicroSleeContainer constructed: bufferSize=%s, preferVT=%s, sbbPool=%s-%s, perVT=%s",
                config.getEventRouterBufferSize(), config.isPreferVirtualThreads(),
                config.getSbbPoolMin(), config.getSbbPoolMax(), config.isSbbPerVirtualThread());
        return new RuntimeValue<MicroSleeContainer>(c);
    }

    /** Start the previously-created container. Idempotent. Called at runtime-init. */
    public void startContainer() {
        if (container != null) {
            LOG.infof("Starting MicroSleeContainer (state=%s)", container.getState());
            container.start();
            LOG.infof("MicroSleeContainer started (state=%s)", container.getState());
        } else {
            LOG.warnf("startContainer() called but container is null");
        }
    }

    /** Stop the previously-started container. Called from the Quarkus shutdown hook. */
    public void stopContainer() {
        if (container != null) {
            LOG.infof("Stopping MicroSleeContainer (state=%s)", container.getState());
            container.stop();
            LOG.infof("MicroSleeContainer stopped");
        }
    }

    /** Register stopContainer() with the Quarkus runtime shutdown context. */
    public void registerShutdownHook(ShutdownContext shutdown) {
        shutdown.addShutdownTask(new Runnable() {
            @Override
            public void run() {
                try {
                    stopContainer();
                } catch (Throwable t) {
                    LOG.errorf(t, "MicroSleeContainer shutdown failed: %s", t.getMessage());
                }
            }
        });
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
                    LOG.warnf("Skipping non-Sbb type %s", fqn);
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
                LOG.infof("Registered pooled SBB type %s", fqn);
            } catch (ClassNotFoundException e) {
                LOG.warnf("Failed to load SBB class %s: %s", fqn, e.getMessage());
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

    /** Derive EventRouter from an already-created container RuntimeValue (STATIC_INIT-safe). */
    public RuntimeValue<EventRouter> eventRouterOf(RuntimeValue<MicroSleeContainer> c) {
        return new RuntimeValue<EventRouter>(c.getValue().getEventRouter());
    }

    /** Derive TimerPort from an already-created container RuntimeValue (STATIC_INIT-safe). */
    public RuntimeValue<TimerPort> timerPortOf(RuntimeValue<MicroSleeContainer> c) {
        return new RuntimeValue<TimerPort>(c.getValue().getTimerPort());
    }

    /** Derive ACNF backend from an already-created container RuntimeValue (STATIC_INIT-safe). */
    public RuntimeValue<com.microjainslee.core.MicroSleeContainer.AcnfBackend> acnfOf(
            RuntimeValue<MicroSleeContainer> c) {
        return new RuntimeValue<com.microjainslee.core.MicroSleeContainer.AcnfBackend>(
                c.getValue().getActivityContextNamingFacility());
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
            LOG.warnf("registerRa() called but container is null (name=%s)", name);
            return;
        }
        if (endpoint == null || command == null) {
            LOG.warnf("registerRa() called with null endpoint or command (name=%s)", name);
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
            LOG.warnf("registerRaFromClassName() called but container is null (class=%s)", className);
            return;
        }
        if (className == null || className.trim().isEmpty()) {
            LOG.warnf("registerRaFromClassName() called with empty class name");
            return;
        }
        try {
            Class<?> clazz = Class.forName(className, true,
                    Thread.currentThread().getContextClassLoader());
            if (!RaEndpointPort.class.isAssignableFrom(clazz)) {
                LOG.warnf("Class %s does not implement RaEndpointPort — skipping", className);
                return;
            }
            if (!RaCommandPort.class.isAssignableFrom(clazz)) {
                LOG.warnf("Class %s does not implement RaCommandPort — skipping", className);
                return;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            RaEndpointPort endpoint = (RaEndpointPort) instance;
            RaCommandPort command = (RaCommandPort) instance;
            container.registerRa(endpoint, command);
            LOG.infof("Registered RA from class name: %s (class=%s)",
                    endpoint.getRaName(), className);
        } catch (ReflectiveOperationException e) {
            LOG.warnf("Failed to instantiate RA class %s: %s", className, e.getMessage());
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
            LOG.warnf("mapEventToSbb() called but container is null (event=%s, sbb=%s)", eventClass, sbbName);
            return;
        }
        if (eventClass == null || eventClass.trim().isEmpty()) {
            LOG.warnf("mapEventToSbb() called with empty event class name (sbb=%s)", sbbName);
            return;
        }
        if (sbbName == null || sbbName.trim().isEmpty()) {
            LOG.warnf("mapEventToSbb() called with empty SBB name (event=%s)", eventClass);
            return;
        }
        try {
            Class<?> rawClass = Class.forName(eventClass, true,
                    Thread.currentThread().getContextClassLoader());
            if (!SleeEvent.class.isAssignableFrom(rawClass)) {
                LOG.warnf("Class %s is not a SleeEvent — skipping mapping to SBB %s", eventClass, sbbName);
                return;
            }
            Class<? extends SleeEvent> eventType = (Class<? extends SleeEvent>) rawClass;
            container.mapEventToSbb(eventType, sbbName);
        } catch (ClassNotFoundException cnfe) {
            LOG.warnf("Event class not found on classpath: %s (sbb=%s) — skipping mapping",
                    eventClass, sbbName);
        }
    }
}