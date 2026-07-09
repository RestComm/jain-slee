/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.collab;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.ra.jss7.transport.Ss7Stack;

/**
 * A protocol-family adapter (MAP, CAP, TCAP …). It registers listeners against
 * the started {@link Ss7Stack} and translates inbound jSS7 callbacks into SLEE
 * events (via {@link Ss7EventPublisher}) and outbound SLEE commands into jSS7
 * provider calls.
 *
 * <p>Adapters are protocol-isolated: each owns its own package subtree so they
 * can be developed independently.</p>
 */
public interface Ss7ProtocolAdapter {

    /** Short protocol name, e.g. {@code "MAP"}, {@code "CAP"}, {@code "TCAP"}. */
    String protocol();

    /**
     * Register listeners against the (already started) stack. Called from
     * {@code raActive()} after {@link Ss7Stack#start()}.
     */
    void attach(Ss7Stack stack, Ss7EventPublisher publisher);

    /** Deregister listeners. Called from {@code raInactive()}. */
    void detach();

    /**
     * Handle an outbound command for this protocol.
     *
     * @return {@code true} if this adapter recognised and handled the command,
     *         {@code false} to let another adapter try.
     */
    default boolean sendOutbound(OutboundCommand command) {
        return false;
    }
}
