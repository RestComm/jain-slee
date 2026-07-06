/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.autonomous;

/**
 * Escalation ladder of the autonomous guardian. Each level implies the
 * relief actions of the levels below it.
 */
public enum PressureLevel {
    /** Heap comfortably below the watermark — nothing to do. */
    NORMAL,
    /** Above the low watermark — trim caches and expire idle state. */
    ELEVATED,
    /** Above the high watermark — compact off-heap arenas, guarded GC. */
    CRITICAL,
    /** Near-OOM — everything above plus the application emergency hook
     *  (shed load, refuse new activities). */
    EMERGENCY
}
