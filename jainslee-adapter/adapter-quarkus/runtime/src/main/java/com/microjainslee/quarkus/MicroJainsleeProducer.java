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

import com.microjainslee.api.AlarmPort;
import com.microjainslee.api.NamingPort;
import com.microjainslee.api.ProfileTablePort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.TimerPort;
import com.microjainslee.api.TracePort;
import com.microjainslee.api.UsagePort;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.InMemoryNamingPort;
import com.microjainslee.core.MicroSleeContainer;
import io.quarkus.arc.Arc;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.RuntimeValue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * CDI producers that re-expose the build-time-constructed {@link MicroSleeContainer} and its
 * key facilities as injectable beans.
 *
 * <p>The container itself is stashed in {@link MicroJainsleeHolder} by the recorder, and
 * each producer pulls it out lazily. If the holder is empty (e.g. when running unit tests
 * that don't go through the Quarkus build), we fall back to a default container built from
 * {@link com.microjainslee.core.MicroSleeConfiguration#defaults()}.</p>
 *
 * <p>Each produced bean is {@link ApplicationScoped} and registered with {@link DefaultBean}
 * so that user code can override any of them by supplying an alternative producer.</p>
 */
public class MicroJainsleeProducer {

    private static final org.jboss.logging.Logger LOG = org.jboss.logging.Logger.getLogger(MicroJainsleeProducer.class);

    private MicroSleeContainer container() {
        RuntimeValue<MicroSleeContainer> rv = MicroJainsleeHolder.get();
        if (rv != null) {
            return rv.getValue();
        }
        LOG.warnf("MicroJainsleeHolder empty — falling back to default MicroSleeContainer (unit-test path?)");
        return new MicroSleeContainer();
    }

    /** Exposes the singleton micro-container. */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public MicroSleeContainer microSleeContainer() {
        return container();
    }

    /** Exposes the LMAX-Disruptor-backed {@link EventRouter} from the singleton container. */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public EventRouter eventRouter() {
        return container().getEventRouter();
    }

    /** Exposes the JAIN-SLEE timer facility from the singleton container. */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public TimerPort timerPort() {
        return container().getTimerPort();
    }

    /** Exposes the in-memory activity-context naming facility from the singleton container. */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public com.microjainslee.core.MicroSleeContainer.AcnfBackend activityContextNamingFacility() {
        return container().getActivityContextNamingFacility();
    }

    /** Exposes the global naming facility (§14). */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public NamingPort namingPort() {
        return new InMemoryNamingPort();
    }

    /** Exposes the alarm facility (§15). */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public AlarmPort alarmPort() {
        return new AlarmPortQuarkusAdapter();
    }

    /** Exposes the profile table port (§10) — in-memory until JPA backend is added. */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public ProfileTablePort profileTablePort() {
        return new ProfileTablePortQuarkusAdapter();
    }

    /** Exposes the usage facility (§12) with optional Micrometer integration. */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public UsagePort usagePort() {
        return new UsageFacilityQuarkusAdapter(resolveMeterRegistry());
    }

    /**
     * Factory for per-SBB tracers (§16).
     */
    @Produces
    @ApplicationScoped
    @DefaultBean
    public TracePort defaultTracePort() {
        return new TraceFacilityQuarkusAdapter("micro-jainslee");
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 2 — CDI-driven RA registration (3-port contract)
    // ──────────────────────────────────────────────────────────

    /**
     * Discover CDI {@link RaEndpointPort}/{@link RaCommandPort} beans and register
     * them on the container. Uses {@code @Observes StartupEvent} (void {@code @Produces}
     * is illegal in CDI).
     */
    void onStart(@jakarta.enterprise.event.Observes io.quarkus.runtime.StartupEvent ev,
                 MicroSleeContainer container,
                 @Any Instance<RaEndpointPort> endpoints,
                 @Any Instance<RaCommandPort> commands) {
        registerResourceAdaptors(container, endpoints, commands);
    }

    private void registerResourceAdaptors(
            MicroSleeContainer container,
            Instance<RaEndpointPort> endpoints,
            Instance<RaCommandPort> commands) {

        // Collect into mutable sorted lists.
        List<RaEndpointPort> epList = new ArrayList<RaEndpointPort>();
        for (RaEndpointPort ep : endpoints) {
            epList.add(ep);
        }
        if (epList.isEmpty()) {
            LOG.debugf("No RaEndpointPort beans discovered; skipping CDI RA registration");
            return;
        }
        Collections.sort(epList, new Comparator<RaEndpointPort>() {
            @Override
            public int compare(RaEndpointPort a, RaEndpointPort b) {
                String na = a.getRaName() != null ? a.getRaName() : "";
                String nb = b.getRaName() != null ? b.getRaName() : "";
                return na.compareTo(nb);
            }
        });

        List<RaCommandPort> cmdList = new ArrayList<RaCommandPort>();
        for (RaCommandPort cmd : commands) {
            cmdList.add(cmd);
        }
        Collections.sort(cmdList, new Comparator<RaCommandPort>() {
            @Override
            public int compare(RaCommandPort a, RaCommandPort b) {
                return a.getClass().getSimpleName().compareTo(b.getClass().getSimpleName());
            }
        });

        int count = Math.min(epList.size(), cmdList.size());
        if (epList.size() != cmdList.size()) {
            LOG.warnf("Mismatch between RaEndpointPort count (%s) and RaCommandPort count (%s); "
                    + "only %s pair(s) will be registered",
                    epList.size(), cmdList.size(), count);
        }

        for (int i = 0; i < count; i++) {
            RaEndpointPort endpoint = epList.get(i);
            RaCommandPort command = cmdList.get(i);
            try {
                container.registerRa(endpoint, command);
                LOG.infof("Registered RA via CDI: %s (endpoint=%s, command=%s)",
                        endpoint.getRaName(),
                        endpoint.getClass().getSimpleName(),
                        command.getClass().getSimpleName());
            } catch (RuntimeException re) {
                LOG.errorf(re, "Failed to register RA [%s]: %s",
                        endpoint.getRaName(), re.getMessage());
            }
        }
        LOG.infof("CDI RA registration complete: %s pair(s) registered", count);
    }

    private static Object resolveMeterRegistry() {
        try {
            Class<?> registryClass = Class.forName("io.micrometer.core.instrument.MeterRegistry");
            if (!Arc.container().isRunning()) {
                return null;
            }
            return Arc.container().select(registryClass).stream().findFirst().orElse(null);
        } catch (Throwable ignored) {
            return null;
        }
    }
}