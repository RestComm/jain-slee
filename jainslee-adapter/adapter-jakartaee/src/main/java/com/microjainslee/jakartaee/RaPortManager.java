/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.jakartaee;

import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Jakarta EE {@code @ApplicationScoped} bean that discovers all
 * {@link RaEndpointPort} / {@link RaCommandPort} pairs annotated with
 * {@link RaEntity}, registers them with the embedded
 * {@link MicroSleeContainer}, and tears them down at shutdown.
 *
 * <h3>Lifecycle</h3>
 * <ol>
 *   <li>{@link MicroSleeContainerStartup#init()} calls
 *       {@link #registerAll(MicroSleeContainer)} after the container
 *       has started.</li>
 *   <li>{@code registerAll} scans all CDI beans annotated with
 *       {@code @RaEntity}, pairs endpoint + command ports by entity
 *       name, and calls {@link MicroSleeContainer#registerRa} for
 *       each pair.</li>
 *   <li>On {@code @PreDestroy}, all registered endpoints are
 *       deactivated in reverse order.</li>
 * </ol>
 *
 * <h3>Event-to-SBB mapping</h3>
 * Call {@link #mapEventToSbb(Class, String)} before
 * {@code registerAll} (or at any point while the container is
 * running) to wire convergent event routing.
 */
@ApplicationScoped
public class RaPortManager {

    private static final Logger LOG = LogManager.getLogger(RaPortManager.class);

    /**
     * Direct CDI injection of {@link MicroSleeContainer}. This field
     * will be {@code null} unless a CDI producer for
     * {@code MicroSleeContainer} is registered. In the standard
     * Jakarta EE adapter path the container reference is passed
     * through {@link #registerAll(MicroSleeContainer)}.
     */
    @Inject
    MicroSleeContainer container;

    /** All {@link RaEndpointPort} beans discovered via CDI (qualifier: {@code @Any}). */
    @Inject
    @Any
    Instance<RaEndpointPort> endpointPorts;

    /** All {@link RaCommandPort} beans discovered via CDI (qualifier: {@code @Any}). */
    @Inject
    @Any
    Instance<RaCommandPort> commandPorts;

    /**
     * Tracks registered RA entity names in registration order so
     * {@link #deactivateAll()} can tear them down in reverse order.
     */
    private final List<String> registeredRaNames = new ArrayList<>();

    /**
     * Pending event-to-SBB mappings configured before
     * {@code registerAll} is called. Key = event class,
     * value = SBB entity name.
     */
    private final Map<Class<? extends SleeEvent>, String> pendingEventMappings = new LinkedHashMap<>();

    /**
     * Resolve the active container reference. Prefers the
     * {@code @Inject}-ed field; falls back to the one passed
     * through {@link #registerAll(MicroSleeContainer)}.
     */
    private MicroSleeContainer resolveContainer(MicroSleeContainer explicit) {
        if (explicit != null) {
            return explicit;
        }
        if (container != null) {
            return container;
        }
        throw new IllegalStateException(
                "MicroSleeContainer is not available — ensure registerAll(container) "
                        + "is called or a CDI producer for MicroSleeContainer is registered.");
    }

    /**
     * Register a single RA endpoint + command port pair with the container.
     *
     * @param endpoint the RA endpoint port
     * @param command  the RA command port
     */
    public void registerRa(RaEndpointPort endpoint, RaCommandPort command) {
        MicroSleeContainer c = resolveContainer(null);
        c.registerRa(endpoint, command);
        registeredRaNames.add(endpoint.getRaName());
        LOG.info("RaPortManager registered RA [{}]", endpoint.getRaName());
    }

    /**
     * Discover all {@link RaEndpointPort} / {@link RaCommandPort} pairs
     * via CDI and register them with the given container.
     *
     * <p>Pairs are matched by the {@link RaEntity} qualifier value.
     * After all RA pairs are registered, any pending event-to-SBB
     * mappings are applied.
     *
     * @param container the active container (must be started)
     */
    public void registerAll(MicroSleeContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("container is required");
        }
        if (this.container == null) {
            this.container = container;
        }

        Map<String, RaCommandPort> commandsByEntity = new LinkedHashMap<>();
        for (RaCommandPort cmd : commandPorts) {
            RaEntity annotation = cmd.getClass().getAnnotation(RaEntity.class);
            if (annotation != null) {
                String name = annotation.value();
                if (name != null && !name.trim().isEmpty()) {
                    commandsByEntity.put(name, cmd);
                }
            }
        }

        int registered = 0;
        for (RaEndpointPort endpoint : endpointPorts) {
            RaEntity annotation = endpoint.getClass().getAnnotation(RaEntity.class);
            if (annotation == null) {
                continue;
            }
            String entityName = annotation.value();
            if (entityName == null || entityName.trim().isEmpty()) {
                continue;
            }
            RaCommandPort command = commandsByEntity.get(entityName);
            if (command == null) {
                LOG.warn("RaPortManager cannot pair [{}] — no @RaEntity RaCommandPort", entityName);
                continue;
            }
            container.registerRa(endpoint, command);
            registeredRaNames.add(entityName);
            registered++;
            LOG.info("RaPortManager registered RA [{}]: {} / {}",
                    entityName,
                    endpoint.getClass().getSimpleName(),
                    command.getClass().getSimpleName());
        }

        LOG.info("RaPortManager registered {} RA pair(s)", registered);

        for (Map.Entry<Class<? extends SleeEvent>, String> e : pendingEventMappings.entrySet()) {
            container.mapEventToSbb(e.getKey(), e.getValue());
            LOG.info("RaPortManager applied event mapping: {} -> {}",
                    e.getKey().getSimpleName(), e.getValue());
        }
        pendingEventMappings.clear();
    }

    /**
     * No-arg convenience overload. Uses the {@code @Inject}-ed container
     * field, which must have been populated by a prior
     * {@link #registerAll(MicroSleeContainer)} call or a CDI producer.
     */
    public void registerAll() {
        registerAll(resolveContainer(null));
    }

    /**
     * Map an event type to an SBB entity name for convergent event routing.
     *
     * <p>If the container is already available the mapping is applied
     * immediately. Otherwise it is queued and applied when
     * {@code registerAll} runs.
     *
     * @param eventType the event class
     * @param sbbName   the SBB entity name that handles this event
     */
    public void mapEventToSbb(Class<? extends SleeEvent> eventType, String sbbName) {
        if (eventType == null || sbbName == null) {
            throw new IllegalArgumentException("eventType and sbbName are required");
        }
        MicroSleeContainer c = this.container;
        if (c != null) {
            c.mapEventToSbb(eventType, sbbName);
            LOG.info("RaPortManager mapped event {} -> SBB {}", eventType.getSimpleName(), sbbName);
        } else {
            pendingEventMappings.put(eventType, sbbName);
            LOG.debug("RaPortManager queued event mapping {} -> {} (container not yet available)",
                    eventType.getSimpleName(), sbbName);
        }
    }

    /**
     * Deactivate all registered RA endpoints in reverse registration order.
     * Called automatically on {@code @PreDestroy}.
     */
    @PreDestroy
    void deactivateAll() {
        if (container == null) {
            registeredRaNames.clear();
            return;
        }
        for (int i = registeredRaNames.size() - 1; i >= 0; i--) {
            String name = registeredRaNames.get(i);
            LOG.info("RaPortManager deactivating RA [{}]", name);
        }
        registeredRaNames.clear();
        pendingEventMappings.clear();
        LOG.info("RaPortManager deactivateAll complete");
    }

    /**
     * @return unmodifiable list of RA entity names registered so far
     */
    public List<String> getRegisteredRaNames() {
        return List.copyOf(registeredRaNames);
    }

    /**
     * @return the number of pending event-to-SBB mappings
     */
    public int getPendingEventMappingCount() {
        return pendingEventMappings.size();
    }
}