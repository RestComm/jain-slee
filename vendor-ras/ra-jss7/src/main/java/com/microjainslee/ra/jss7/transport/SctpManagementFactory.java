/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.transport;

import org.mobicents.protocols.api.Management;

/**
 * The single isolation point for the concrete SCTP transport implementation.
 *
 * <p>The rest of the RA programs exclusively against the SCTP project's
 * {@link org.mobicents.protocols.api.Management} abstraction; only this factory
 * knows the concrete provider, so swapping the SCTP implementation (or bumping
 * its version) is a one-line change here rather than a change scattered across
 * the transport code.</p>
 *
 * <p>The concrete class is resolved reflectively so no transport/impl class is
 * referenced (imported) directly by the RA; the SCTP jar remains a runtime
 * dependency of the {@code sctp} project only.</p>
 */
final class SctpManagementFactory {

    /** Default SCTP provider (mobicents Netty-based impl). */
    private static final String DEFAULT_IMPL =
            "org.mobicents.protocols.sctp.netty.NettySctpManagementImpl";

    private SctpManagementFactory() { }

    /**
     * Create an SCTP {@link Management} instance from the SCTP project.
     *
     * @param name management/stack name
     * @return a not-yet-started {@link Management}
     */
    static Management create(String name) throws Exception {
        String impl = System.getProperty("ra.jss7.sctp.impl", DEFAULT_IMPL);
        Class<?> clazz = Class.forName(impl);
        return (Management) clazz.getConstructor(String.class).newInstance(name);
    }
}
