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

import com.microjainslee.api.ActivityContextNamingFacility;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.TimerPort;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * EJB Singleton driving the lifecycle of the embedded micro-jainslee
 * container inside any Jakarta EE 9+ runtime (WildFly 27+, Payara 6+,
 * Open Liberty, TomEE 9+).
 */
@Singleton
@Startup
@LocalBean
public class MicroSleeContainerStartup {

    private static final Logger LOG = LogManager.getLogger(MicroSleeContainerStartup.class);
    public static final String CONFIG_PROPERTY_PREFIX = "microjainslee.config.";
    private MicroSleeContainer container;

    @Inject
    RaPortManager raPortManager;

    @PostConstruct
    void init() {
        LOG.info("Jakarta EE adapter init — building MicroSleeContainer");
        MicroSleeConfiguration cfg = buildConfigurationFromSystemProperties();
        LOG.info("MicroSleeContainer configuration: bufferSize={}, preferVT={}, sbbPerVT={}, sbbPool={}-{}",
                cfg.getEventRouterBufferSize(),
                cfg.isPreferVirtualThreads(),
                cfg.isSbbPerVirtualThread(),
                cfg.getSbbPoolMin(),
                cfg.getSbbPoolMax());
        this.container = new MicroSleeContainer(cfg);
        this.container.start();
        LOG.info("MicroSleeContainer started (state={})", container.getState());
        bindAll(container);
        raPortManager.registerAll(container);
    }

    @PreDestroy
    void shutdown() {
        LOG.info("Jakarta EE adapter stopping");
        if (container != null) {
            try {
                container.stop();
                LOG.info("MicroSleeContainer stopped");
            } catch (RuntimeException e) {
                LOG.warn("MicroSleeContainer.stop() threw — continuing with JNDI cleanup", e);
            }
        }
        unbindAll();
    }

    public MicroSleeContainer getContainer() {
        if (container == null) {
            throw new IllegalStateException("MicroSleeContainer not yet initialised");
        }
        return container;
    }

    private static void bindAll(MicroSleeContainer c) {
        bind(JndiNames.CONTAINER, c, "MicroSleeContainer");
        bind(JndiNames.EVENT_ROUTER, c.getEventRouter(), "EventRouter");
        bind(JndiNames.TIMER_PORT, c.getTimerPort(), "TimerPort");
        bind(JndiNames.ACNF, c.getActivityContextNamingFacility(), "ActivityContextNamingFacility");
    }

    private static void bind(String jndiName, Object value, String label) {
        try {
            InitialContext ctx = new InitialContext();
            try {
                ctx.bind(jndiName, value);
                LOG.info("JNDI bound: {} -> {} ({})", jndiName,
                        value == null ? "null" : value.getClass().getSimpleName(), label);
            } catch (NameAlreadyBoundException alreadyBound) {
                ctx.rebind(jndiName, value);
                LOG.warn("JNDI entry {} already bound — rebound ({})", jndiName, label);
            } finally {
                ctx.close();
            }
        } catch (NamingException e) {
            LOG.error("Failed to bind {} at {}: {}", label, jndiName, e.getMessage(), e);
        }
    }

    private static void unbindAll() {
        unbind(JndiNames.CONTAINER, "MicroSleeContainer");
        unbind(JndiNames.EVENT_ROUTER, "EventRouter");
        unbind(JndiNames.TIMER_PORT, "TimerPort");
        unbind(JndiNames.ACNF, "ActivityContextNamingFacility");
    }

    private static void unbind(String jndiName, String label) {
        try {
            InitialContext ctx = new InitialContext();
            try {
                ctx.unbind(jndiName);
                LOG.info("JNDI unbound: {} ({})", jndiName, label);
            } catch (NamingException notPresent) {
                LOG.warn("JNDI entry already absent at shutdown: {} ({})", jndiName, label);
            } finally {
                ctx.close();
            }
        } catch (NamingException e) {
            LOG.warn("Could not open InitialContext to unbind {} ({}): {}", jndiName, label, e.getMessage());
        }
    }

    private static MicroSleeConfiguration buildConfigurationFromSystemProperties() {
        MicroSleeConfiguration.Builder b = MicroSleeConfiguration.builder();
        applyInt(b, "eventRouterBufferSize", (builder, v) -> builder.eventRouterBufferSize(v));
        applyBoolean(b, "preferVirtualThreads", (builder, v) -> builder.preferVirtualThreads(v));
        applyBoolean(b, "sbbPerVirtualThread", (builder, v) -> builder.sbbPerVirtualThread(v));
        applyInt(b, "sbbPoolMin", (builder, v) -> builder.sbbPoolMin(v));
        applyInt(b, "sbbPoolMax", (builder, v) -> builder.sbbPoolMax(v));
        try {
            return b.build();
        } catch (IllegalArgumentException invalid) {
            LOG.warn("Invalid microjainslee.config.* — falling back to defaults: {}", invalid.getMessage());
            return MicroSleeConfiguration.defaults();
        }
    }

    @FunctionalInterface
    private interface IntSetter {
        MicroSleeConfiguration.Builder apply(MicroSleeConfiguration.Builder b, int value);
    }

    private static void applyInt(MicroSleeConfiguration.Builder builder, String key, IntSetter setter) {
        String property = System.getProperty(CONFIG_PROPERTY_PREFIX + key);
        if (property == null || property.isEmpty()) return;
        try {
            int value = Integer.parseInt(property.trim());
            setter.apply(builder, value);
            LOG.debug("Applied config property: {}{}={}", CONFIG_PROPERTY_PREFIX, key, value);
        } catch (NumberFormatException e) {
            LOG.warn("Ignoring non-numeric microjainslee.config.{}={}", key, property);
        }
    }

    @FunctionalInterface
    private interface BooleanSetter {
        MicroSleeConfiguration.Builder apply(MicroSleeConfiguration.Builder b, boolean value);
    }

    private static void applyBoolean(MicroSleeConfiguration.Builder builder, String key, BooleanSetter setter) {
        String property = System.getProperty(CONFIG_PROPERTY_PREFIX + key);
        if (property == null || property.isEmpty()) return;
        Boolean parsed = parseBoolean(property.trim());
        if (parsed == null) {
            LOG.warn("Ignoring non-boolean microjainslee.config.{}={}", key, property);
            return;
        }
        setter.apply(builder, parsed);
        LOG.debug("Applied config property: {}{}={}", CONFIG_PROPERTY_PREFIX, key, parsed);
    }

    private static Boolean parseBoolean(String raw) {
        String v = raw.toLowerCase();
        if ("true".equals(v) || "yes".equals(v) || "on".equals(v) || "1".equals(v)) return Boolean.TRUE;
        if ("false".equals(v) || "no".equals(v) || "off".equals(v) || "0".equals(v)) return Boolean.FALSE;
        return null;
    }
}
