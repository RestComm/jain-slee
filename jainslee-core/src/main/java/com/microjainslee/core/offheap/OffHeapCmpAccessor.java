/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import java.nio.charset.StandardCharsets;

/**
 * Typed slot accessors used by generated {@code $Concrete} classes (and
 * directly by tests). All methods take {@code (layout, base, fieldIndex)}
 * — a bound slot base address plus a compile-time-constant field index —
 * and perform zero Map lookups and zero boxing. String reads allocate
 * exactly the returned String (unavoidable until the P3 zero-copy view);
 * primitive reads/writes allocate nothing.
 *
 * <p>All accessors are unbound-safe: {@code base == 0} returns the Java
 * default ({@code null}/0/false) on read and is a silent no-op on write,
 * which is what the {@code fallback} migration mode relies on.</p>
 */
public final class OffHeapCmpAccessor {

    private OffHeapCmpAccessor() {
    }

    // ── directory helpers ───────────────────────────────────────────

    private static long dirValueAddr(OffHeapLayout layout, long base, int idx) {
        return layout.dirEntryAddr(base, idx) + 4; // [2B hash][2B type][4B value/offset]
    }

    // ── int ─────────────────────────────────────────────────────────

    public static int readInt(OffHeapLayout layout, long base, int idx) {
        if (base == 0L) return 0;
        return OffHeapMemory.getInt(dirValueAddr(layout, base, idx));
    }

    public static void writeInt(OffHeapLayout layout, long base, int idx, int value) {
        if (base == 0L) return;
        OffHeapMemory.putInt(dirValueAddr(layout, base, idx), value);
        markDirty(base);
    }

    // ── boolean ─────────────────────────────────────────────────────

    public static boolean readBoolean(OffHeapLayout layout, long base, int idx) {
        return readInt(layout, base, idx) != 0;
    }

    public static void writeBoolean(OffHeapLayout layout, long base, int idx, boolean value) {
        writeInt(layout, base, idx, value ? 1 : 0);
    }

    // ── long / double (payload area, 8B) ────────────────────────────

    public static long readLong(OffHeapLayout layout, long base, int idx) {
        if (base == 0L) return 0L;
        return OffHeapMemory.getLong(base + layout.payloadOffset(idx));
    }

    public static void writeLong(OffHeapLayout layout, long base, int idx, long value) {
        if (base == 0L) return;
        OffHeapMemory.putLong(base + layout.payloadOffset(idx), value);
        markDirty(base);
    }

    public static double readDouble(OffHeapLayout layout, long base, int idx) {
        return Double.longBitsToDouble(readLong(layout, base, idx));
    }

    public static void writeDouble(OffHeapLayout layout, long base, int idx, double value) {
        writeLong(layout, base, idx, Double.doubleToRawLongBits(value));
    }

    // ── String ([2B len][UTF-8], null → dir offset 0) ───────────────

    public static String readString(OffHeapLayout layout, long base, int idx) {
        if (base == 0L) return null;
        int payOff = OffHeapMemory.getInt(dirValueAddr(layout, base, idx));
        if (payOff == 0) return null;
        long addr = base + payOff;
        int len = OffHeapMemory.getShort(addr) & 0xFFFF;
        if (len == 0) return "";
        byte[] buf = new byte[len];
        OffHeapMemory.copyOut(addr + 2, buf, 0, len);
        return new String(buf, StandardCharsets.UTF_8);
    }

    public static void writeString(OffHeapLayout layout, long base, int idx, String value) {
        if (base == 0L) return;
        long dirAddr = dirValueAddr(layout, base, idx);
        if (value == null) {
            OffHeapMemory.putInt(dirAddr, 0);   // null sentinel
            markDirty(base);
            return;
        }
        int payOff = layout.payloadOffset(idx);
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        int budget = layout.field(idx).maxBytes();
        int len = Math.min(bytes.length, budget);   // truncation per design §12
        long addr = base + payOff;
        OffHeapMemory.putShort(addr, (short) len);
        if (len > 0) {
            OffHeapMemory.copyIn(bytes, 0, addr + 2, len);
        }
        OffHeapMemory.putInt(dirAddr, payOff);
        markDirty(base);
    }

    // ── byte[] ([4B len][bytes], null → dir offset 0) ───────────────

    public static byte[] readBytes(OffHeapLayout layout, long base, int idx) {
        if (base == 0L) return null;
        int payOff = OffHeapMemory.getInt(dirValueAddr(layout, base, idx));
        if (payOff == 0) return null;
        long addr = base + payOff;
        int len = OffHeapMemory.getInt(addr);
        byte[] buf = new byte[len];
        if (len > 0) {
            OffHeapMemory.copyOut(addr + 4, buf, 0, len);
        }
        return buf;
    }

    public static void writeBytes(OffHeapLayout layout, long base, int idx, byte[] value) {
        if (base == 0L) return;
        long dirAddr = dirValueAddr(layout, base, idx);
        if (value == null) {
            OffHeapMemory.putInt(dirAddr, 0);
            markDirty(base);
            return;
        }
        int payOff = layout.payloadOffset(idx);
        int budget = layout.field(idx).maxBytes();
        int len = Math.min(value.length, budget);
        long addr = base + payOff;
        OffHeapMemory.putInt(addr, len);
        if (len > 0) {
            OffHeapMemory.copyIn(value, 0, addr + 4, len);
        }
        OffHeapMemory.putInt(dirAddr, payOff);
        markDirty(base);
    }

    // ── generic (name-based; registration/diagnostic path, not hot) ─

    public static Object read(OffHeapLayout layout, long base, String fieldName) {
        int idx = layout.indexOf(fieldName);
        return switch (layout.field(idx).type()) {
            case OffHeapLayout.T_INT -> readInt(layout, base, idx);
            case OffHeapLayout.T_LONG -> readLong(layout, base, idx);
            case OffHeapLayout.T_BOOLEAN -> readBoolean(layout, base, idx);
            case OffHeapLayout.T_DOUBLE -> readDouble(layout, base, idx);
            case OffHeapLayout.T_STRING -> readString(layout, base, idx);
            case OffHeapLayout.T_BYTES -> readBytes(layout, base, idx);
            default -> throw new IllegalStateException();
        };
    }

    public static void write(OffHeapLayout layout, long base, String fieldName, Object value) {
        int idx = layout.indexOf(fieldName);
        switch (layout.field(idx).type()) {
            case OffHeapLayout.T_INT -> writeInt(layout, base, idx,
                    value == null ? 0 : ((Number) value).intValue());
            case OffHeapLayout.T_LONG -> writeLong(layout, base, idx,
                    value == null ? 0L : ((Number) value).longValue());
            case OffHeapLayout.T_BOOLEAN -> writeBoolean(layout, base, idx,
                    value != null && (Boolean) value);
            case OffHeapLayout.T_DOUBLE -> writeDouble(layout, base, idx,
                    value == null ? 0d : ((Number) value).doubleValue());
            case OffHeapLayout.T_STRING -> writeString(layout, base, idx, (String) value);
            case OffHeapLayout.T_BYTES -> writeBytes(layout, base, idx, (byte[]) value);
            default -> throw new IllegalStateException();
        }
    }

    private static void markDirty(long base) {
        OffHeapMemory.putShort(base + OffHeapLayout.OFF_FLAGS,
                (short) (OffHeapMemory.getShort(base + OffHeapLayout.OFF_FLAGS)
                        | OffHeapLayout.FLAG_DIRTY));
    }
}
