/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.autonomous;

/**
 * A component that can give memory back on demand (cache, buffer, arena,
 * registry…). Participants register with the {@link AutonomousGuardian};
 * under pressure the guardian calls {@link #relieve(PressureLevel)} on
 * each of them — the runtime never guesses what is safe to drop, the
 * owner of the state decides.
 *
 * <p>Implementations must be fast, idempotent and safe to call from a
 * JMX notification thread. They must never call back into the guardian.</p>
 */
@FunctionalInterface
public interface MemoryReliefParticipant {

    /** Stable name for logs/metrics. */
    String name();

    /**
     * Release what is appropriate for {@code level}.
     *
     * @return an indicative count of released items (entries evicted,
     *         slots compacted, bytes freed / 1024 — any monotonic measure;
     *         used only for logging and tests)
     */
    long relieve(PressureLevel level);
}
