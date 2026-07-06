/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.api.annotations;

/** Backing storage for {@link OffHeap} SBB state. */
public enum StorageType {
    /** DirectByteBuffer — fastest, zero GC, does not survive restart. */
    DIRECT,
    /** Memory-mapped file — zero GC, survives JVM restart (crash recovery). */
    MMAP
}
