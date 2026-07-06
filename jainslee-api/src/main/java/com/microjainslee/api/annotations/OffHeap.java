/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Opt-in off-heap CMP state for an SBB type (design:
 * docs/en/design-offheap-sbb-state.md).
 *
 * <p>CMP fields of the annotated SBB are stored in fixed-size slots inside
 * a per-type {@code OffHeapArena} (direct memory or a memory-mapped file)
 * instead of heap maps — zero GC pressure and zero Map allocation on the
 * CMP hot path. The {@code @CmpField} programming model is unchanged.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface OffHeap {

    StorageType storage() default StorageType.DIRECT;

    /** Slot size in bytes; 0 = auto-calculate from the CMP field layout. */
    int slotSize() default 0;

    /** Maximum entities of this type (arena capacity = slotSize × maxSlots). */
    int maxSlots() default 1_048_576;

    /** Backing file path (MMAP storage only). */
    String filePath() default "";

    /**
     * Emit dual accessors: off-heap when a slot is bound, original heap
     * path when unbound. Lets one SBB type run in both modes (migration).
     */
    boolean fallback() default false;

    /** Per-String/byte[] field payload budget in bytes (values are truncated beyond it). */
    int maxFieldBytes() default 64;
}
