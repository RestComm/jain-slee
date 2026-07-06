/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.collab;

import com.microjainslee.api.SleeEvent;

import java.util.Map;

/**
 * Collaborator: turns a consumed Camel exchange into the {@link SleeEvent}
 * fired at SBBs. The default produces the generic
 * {@code CamelInboundEvent}; applications plug their own factory to emit
 * typed per-domain events (and route them with {@code mapEventToSbb}).
 *
 * <p>Every RA in vendor-ras follows the same template — {@code events/}
 * (what SBBs receive), {@code command/} (what SBBs send),
 * {@code collab/} (pluggable strategy + registry pieces like this one).</p>
 */
@FunctionalInterface
public interface CamelEventFactory {

    SleeEvent create(String endpointUri, String exchangeId, String activityId,
                     Object body, Map<String, Object> headers, boolean requiresReply);
}
