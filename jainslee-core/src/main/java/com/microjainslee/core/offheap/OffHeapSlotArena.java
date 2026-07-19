/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

/**
 * Shared contract for off-heap slot arenas — implemented by both
 * {@link OffHeapArena} (JDK DirectByteBuffer) and
 * {@link AgronaOffHeapArena} (Agrona UnsafeBuffer, P2).
 *
 * <p>The interface surface is deliberately narrow: allocate, free,
 * resolve, compact, and read-only metrics. This is enough for the
 * {@code VirtualThreadSbbEntityPool} hot path and the autonomous
 * guardian's compaction trigger.</p>
 */
public interface OffHeapSlotArena extends AutoCloseable {

    /** Human-readable arena name (SBB type simple name). */
    String name();

    /** Field layout shared by every slot in this arena. */
    OffHeapLayout layout();

    /** Maximum number of slots this arena supports. */
    int maxSlots();

    /**
     * Allocate (or re-claim) a slot for {@code entityId}.
     * @return base address of the slot (stable for the entity's lifetime)
     */
    long allocate(String entityId);

    /** Release the entity's slot back to the free list (zero-filled). */
    void free(String entityId);

    /** Base address for a live entity, or 0 when absent. */
    long resolve(String entityId);

    /**
     * Compact the arena: move top occupied slots down into free gaps.
     * @return number of entities moved (0 = nothing to compact)
     */
    int compact();

    /** Number of currently allocated slots. */
    int occupiedCount();

    /** Number of slots on the free list (reusable holes). */
    int freeListSize();

    /** Highest slot index ever allocated (high-water mark). */
    int highWaterMark();

    /** Free slots as a fraction of allocated slots (0.0–1.0). */
    double fragmentationRatio();
}
