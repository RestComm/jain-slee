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

/**
 * Canonical JNDI names published by the Jakarta EE 9+ adapter.
 *
 * <p>All bindings live under {@code java:global/microjainslee/} so they are
 * visible to every Jakarta EE component (EJBs, CDI beans, servlets, JSF
 * managed beans, JCA connectors) regardless of the application module that
 * performs the lookup. The {@code java:global} namespace is the portable
 * binding scope for shared resources in Jakarta EE 9+ and is honoured by
 * WildFly 27+, Payara 6+, Open Liberty, and TomEE 9+.</p>
 *
 * <p>Names mirror the JAIN-SLEE 1.1 facilities exposed by
 * {@code MicroSleeContainer}: the container itself, the LMAX-Disruptor-backed
 * {@code EventRouter}, the {@code TimerPort}, and the
 * {@code ActivityContextNamingFacility}.</p>
 */
public final class JndiNames {

    /**
     * The embedded {@code MicroSleeContainer} itself.
     * Lookup yields {@code com.microjainslee.core.MicroSleeContainer}.
     */
    public static final String CONTAINER =
            "java:global/microjainslee/Container";

    /**
     * The LMAX-Disruptor-backed event router.
     * Lookup yields {@code com.microjainslee.core.EventRouter}.
     */
    public static final String EVENT_ROUTER =
            "java:global/microjainslee/EventRouter";

    /**
     * The JAIN-SLEE 1.1 timer facility.
     * Lookup yields {@code com.microjainslee.api.TimerPort}.
     */
    public static final String TIMER_PORT =
            "java:global/microjainslee/TimerPort";

    /**
     * The JAIN-SLEE 1.1 §6.2 Activity Context Naming Facility.
     * Lookup yields {@code com.microjainslee.api.ActivityContextNamingFacility}
     * (the in-memory implementation is bind-compatible with this interface).
     */
    public static final String ACNF =
            "java:global/microjainslee/ActivityContextNamingFacility";

    /**
     * JNDI prefix for RA endpoint port bindings.
     * Lookup yields {@code com.microjainslee.api.RaEndpointPort}.
     */
    public static final String RA_ENDPOINT_PORT_PREFIX = "java:comp/env/microjainslee/ra/endpoint/";

    /**
     * JNDI prefix for RA command port bindings.
     * Lookup yields {@code com.microjainslee.api.RaCommandPort}.
     */
    public static final String RA_COMMAND_PORT_PREFIX = "java:comp/env/microjainslee/ra/command/";

    private JndiNames() {
        // No instances — constants only.
    }
}