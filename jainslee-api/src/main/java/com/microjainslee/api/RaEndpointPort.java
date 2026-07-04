/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.api;

/**
 * 3-port contract — the first-class API that every Resource Adaptor exposes
 * to the micro-jainslee container.
 *
 * <h3>Lifecycle</h3>
 * <ol>
 *   <li>Container calls {@link #activate(RaBootstrapPort)} — RA receives its
 *       bootstrap port and may start I/O, timers, etc.</li>
 *   <li>RA processes commands via its {@link RaCommandPort} while active.</li>
 *   <li>Container calls {@link #deactivate()} — RA stops all I/O and releases
 *       resources.</li>
 * </ol>
 *
 * <h3>Discovery</h3>
 * {@link #getRaName()} returns the logical RA entity name so the container can
 * route {@code OutboundCommand}s to the correct RA instance.
 *
 * @see RaBootstrapPort
 * @see RaCommandPort
 */
public interface RaEndpointPort {

    /**
     * Activate this RA endpoint. The container passes in a
     * {@link RaBootstrapPort} that the RA uses to create activity handles
     * and fire events back into the SLEE event router.
     *
     * @param bootstrap the bootstrap port for this RA instance
     */
    void activate(RaBootstrapPort bootstrap);

    /**
     * Deactivate this RA endpoint. After this call the RA must release all
     * protocol resources (sockets, timers, etc.) and stop firing events.
     */
    void deactivate();

    /**
     * @return the logical RA entity name, unique within the SLEE container
     */
    String getRaName();
}
