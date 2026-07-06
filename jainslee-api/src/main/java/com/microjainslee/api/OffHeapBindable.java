/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.api;

/**
 * Implemented by generated {@code $Concrete} SBB classes whose CMP state
 * lives off-heap. The entity pool binds/unbinds the slot around the
 * entity lifecycle; accessors read {@code _offHeapBase} directly — a
 * single field read, no arena lookup per access.
 */
public interface OffHeapBindable {

    /** Bind this object to its off-heap slot (base address). */
    void bindSlot(long baseAddr);

    /** Detach from the slot; subsequent reads return defaults/null. */
    void unbindSlot();

    /** Entity id backing this slot (recovery + diagnostics). */
    void setEntityId(String entityId);

    /** Current slot base address, 0 when unbound. */
    long slotAddress();
}
