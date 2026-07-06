/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Compile-once slot layout for one SBB type (design doc §4).
 *
 * <p>Slot format (fixed-size):</p>
 * <pre>
 *   0            4B   magic 0xC0FFEE01
 *   4            2B   flags: bit0 = occupied, bit1 = dirty
 *   6            2B   field count N
 *   8            N×8B field directory: [2B nameHash][2B type][4B payloadOffset|inline]
 *   8+N×8        var  payload area (pre-assigned, fixed offsets per field)
 *   size-100     2B+94B reserved entityId region (recovery)
 *   size-4       4B   CRC32 of bytes [0, size-4)  (mmap commit protocol)
 * </pre>
 *
 * <p>Payload offsets are assigned once at layout time, so setters never
 * allocate or move data — a String field always writes into its own
 * pre-reserved region ([2B len][bytes], truncated to its budget).</p>
 */
public final class OffHeapLayout {

    public static final int MAGIC = 0xC0FFEE01;
    public static final int OFF_MAGIC = 0;
    public static final int OFF_FLAGS = 4;
    public static final int OFF_FIELD_COUNT = 6;
    public static final int OFF_DIRECTORY = 8;
    public static final int DIR_ENTRY_SIZE = 8;
    public static final int ENTITY_ID_RESERVE = 96; // 2B len + 94B UTF-8
    public static final int CRC_SIZE = 4;

    public static final short FLAG_OCCUPIED = 1;
    public static final short FLAG_DIRTY = 2;

    /** Field types (directory 2B type codes) — design doc §4. */
    public static final int T_INT = 0;
    public static final int T_LONG = 1;
    public static final int T_STRING = 2;
    public static final int T_BYTES = 3;
    public static final int T_BOOLEAN = 4;
    public static final int T_DOUBLE = 5;

    /** One CMP field in the layout. */
    public record FieldSpec(String name, int type, int maxBytes) {

        public static FieldSpec ofInt(String name)     { return new FieldSpec(name, T_INT, 4); }
        public static FieldSpec ofLong(String name)    { return new FieldSpec(name, T_LONG, 8); }
        public static FieldSpec ofBoolean(String name) { return new FieldSpec(name, T_BOOLEAN, 1); }
        public static FieldSpec ofDouble(String name)  { return new FieldSpec(name, T_DOUBLE, 8); }
        public static FieldSpec ofString(String name, int maxBytes) {
            return new FieldSpec(name, T_STRING, maxBytes);
        }
        public static FieldSpec ofBytes(String name, int maxBytes) {
            return new FieldSpec(name, T_BYTES, maxBytes);
        }

        /** Map a Java type to a spec (used when reflecting @CmpField accessors). */
        public static FieldSpec forJavaType(String name, Class<?> javaType, int defaultMaxBytes) {
            if (javaType == int.class || javaType == Integer.class) return ofInt(name);
            if (javaType == long.class || javaType == Long.class) return ofLong(name);
            if (javaType == boolean.class || javaType == Boolean.class) return ofBoolean(name);
            if (javaType == double.class || javaType == Double.class) return ofDouble(name);
            if (javaType == String.class) return ofString(name, defaultMaxBytes);
            if (javaType == byte[].class) return ofBytes(name, defaultMaxBytes);
            throw new IllegalArgumentException("Off-heap CMP does not support type "
                    + javaType.getName() + " (field '" + name + "')");
        }

        boolean isInline() {
            return type == T_INT || type == T_BOOLEAN;
        }

        /** Bytes reserved in the payload area (0 for inline fields). */
        int payloadReserve() {
            return switch (type) {
                case T_INT, T_BOOLEAN -> 0;                    // inline in directory
                case T_LONG, T_DOUBLE -> 8;
                case T_STRING -> align4(2 + maxBytes);          // [2B len][bytes]
                case T_BYTES -> align4(4 + maxBytes);           // [4B len][bytes]
                default -> throw new IllegalStateException("type " + type);
            };
        }
    }

    private final List<FieldSpec> fields;
    private final int slotSize;
    private final int[] payloadOffsets;   // per field; 0 for inline fields
    private final short[] nameHashes;

    private OffHeapLayout(List<FieldSpec> fields, int slotSize,
                          int[] payloadOffsets, short[] nameHashes) {
        this.fields = fields;
        this.slotSize = slotSize;
        this.payloadOffsets = payloadOffsets;
        this.nameHashes = nameHashes;
    }

    /**
     * Build a layout. {@code requestedSlotSize} 0 → auto-size (rounded up
     * to the next power of two, min 128). An explicit size that cannot fit
     * the fields fails fast at deploy time — never silently at runtime.
     */
    public static OffHeapLayout of(List<FieldSpec> fieldSpecs, int requestedSlotSize) {
        if (fieldSpecs.isEmpty()) {
            throw new IllegalArgumentException("Off-heap layout needs at least one CMP field");
        }
        if (fieldSpecs.size() > 255) {
            throw new IllegalArgumentException("At most 255 CMP fields per SBB type");
        }
        List<FieldSpec> fields = List.copyOf(fieldSpecs);
        int[] offsets = new int[fields.size()];
        short[] hashes = new short[fields.size()];
        int payloadCursor = OFF_DIRECTORY + fields.size() * DIR_ENTRY_SIZE;
        for (int i = 0; i < fields.size(); i++) {
            FieldSpec f = fields.get(i);
            hashes[i] = nameHash(f.name());
            if (f.isInline()) {
                offsets[i] = 0;
            } else {
                offsets[i] = payloadCursor;
                payloadCursor += f.payloadReserve();
            }
        }
        int required = payloadCursor + ENTITY_ID_RESERVE + CRC_SIZE;
        int slotSize = requestedSlotSize;
        if (slotSize <= 0) {
            slotSize = Integer.highestOneBit(Math.max(128, required));
            if (slotSize < required) slotSize <<= 1;
        } else if (slotSize < required) {
            throw new IllegalArgumentException("slotSize " + slotSize
                    + " too small — layout needs " + required + " bytes ("
                    + fields.size() + " fields)");
        }
        return new OffHeapLayout(fields, slotSize, offsets, hashes);
    }

    public int slotSize() { return slotSize; }
    public int fieldCount() { return fields.size(); }
    public List<FieldSpec> fields() { return Collections.unmodifiableList(fields); }
    public FieldSpec field(int index) { return fields.get(index); }
    public int payloadOffset(int index) { return payloadOffsets[index]; }
    public short nameHash(int index) { return nameHashes[index]; }
    public int crcOffset() { return slotSize - CRC_SIZE; }
    public int entityIdOffset() { return slotSize - CRC_SIZE - ENTITY_ID_RESERVE; }
    public long dirEntryAddr(long base, int index) {
        return base + OFF_DIRECTORY + (long) index * DIR_ENTRY_SIZE;
    }

    public int indexOf(String fieldName) {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).name().equals(fieldName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown CMP field '" + fieldName + "' — layout has "
                + fields.stream().map(FieldSpec::name).toList());
    }

    static short nameHash(String name) {
        int h = name.toLowerCase(Locale.ROOT).hashCode();
        return (short) (h ^ (h >>> 16));
    }

    static int align4(int v) {
        return (v + 3) & ~3;
    }
}
