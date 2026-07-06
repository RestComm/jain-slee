/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import sun.misc.Unsafe;

/**
 * Single confinement point for {@code sun.misc.Unsafe} in the off-heap
 * subsystem. Everything else calls the typed helpers here, so a future
 * migration to the FFM API ({@code java.lang.foreign.MemorySegment}) —
 * or a GraalVM substitution — touches exactly one class.
 *
 * <p>Only memory-access methods are used (no thread control), which is
 * fully virtual-thread and native-image compatible.</p>
 */
public final class OffHeapMemory {

    private static final Unsafe UNSAFE;
    private static final long BUFFER_ADDRESS_OFFSET;
    public static final long BYTE_ARRAY_BASE = Unsafe.ARRAY_BYTE_BASE_OFFSET;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
            BUFFER_ADDRESS_OFFSET = UNSAFE.objectFieldOffset(
                    Buffer.class.getDeclaredField("address"));
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private OffHeapMemory() {
    }

    /** Native base address of a direct/mapped buffer. */
    public static long addressOf(ByteBuffer directBuffer) {
        if (!directBuffer.isDirect()) {
            throw new IllegalArgumentException("Buffer must be direct");
        }
        return UNSAFE.getLong(directBuffer, BUFFER_ADDRESS_OFFSET);
    }

    public static int getInt(long addr) { return UNSAFE.getInt(addr); }
    public static void putInt(long addr, int v) { UNSAFE.putInt(addr, v); }

    public static long getLong(long addr) { return UNSAFE.getLong(addr); }
    public static void putLong(long addr, long v) { UNSAFE.putLong(addr, v); }

    public static short getShort(long addr) { return UNSAFE.getShort(addr); }
    public static void putShort(long addr, short v) { UNSAFE.putShort(addr, v); }

    public static byte getByte(long addr) { return UNSAFE.getByte(addr); }
    public static void putByte(long addr, byte v) { UNSAFE.putByte(addr, v); }

    public static void copyIn(byte[] src, int srcOff, long dstAddr, int len) {
        UNSAFE.copyMemory(src, BYTE_ARRAY_BASE + srcOff, null, dstAddr, len);
    }

    public static void copyOut(long srcAddr, byte[] dst, int dstOff, int len) {
        UNSAFE.copyMemory(null, srcAddr, dst, BYTE_ARRAY_BASE + dstOff, len);
    }

    /** Off-heap → off-heap move (compaction). */
    public static void copy(long srcAddr, long dstAddr, long len) {
        UNSAFE.copyMemory(srcAddr, dstAddr, len);
    }

    public static void zero(long addr, long len) {
        UNSAFE.setMemory(addr, len, (byte) 0);
    }
}
