/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import com.microjainslee.core.offheap.OffHeapLayout.FieldSpec;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Full case matrix for the off-heap slot store
 * (docs/en/design-offheap-sbb-state.md): layout sizing, every field type,
 * null/empty/truncation, unbound guards, slot reuse, exhaustion,
 * isolation, compaction with rebinding, mmap recovery + CRC torn-write
 * protocol.
 */
public class OffHeapArenaTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static OffHeapLayout layout() {
        return OffHeapLayout.of(List.of(
                FieldSpec.ofString("msisdn", 20),
                FieldSpec.ofString("sessionId", 64),
                FieldSpec.ofInt("menuState"),
                FieldSpec.ofInt("retryCount"),
                FieldSpec.ofLong("startedAt"),
                FieldSpec.ofBoolean("premium"),
                FieldSpec.ofDouble("balance"),
                FieldSpec.ofBytes("token", 16)), 0);
    }

    // ── layout ──────────────────────────────────────────────────────

    @Test
    public void autoSlotSizeIsPowerOfTwoAndFitsAllFields() {
        OffHeapLayout l = layout();
        assertTrue("slot must be a power of two: " + l.slotSize(),
                Integer.bitCount(l.slotSize()) == 1);
        assertTrue(l.slotSize() >= OffHeapLayout.OFF_DIRECTORY
                + l.fieldCount() * OffHeapLayout.DIR_ENTRY_SIZE
                + OffHeapLayout.ENTITY_ID_RESERVE + OffHeapLayout.CRC_SIZE);
    }

    @Test
    public void explicitSlotSizeTooSmallFailsAtDeployTime() {
        try {
            OffHeapLayout.of(List.of(FieldSpec.ofString("big", 500)), 128);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("too small"));
        }
    }

    @Test
    public void unsupportedFieldTypeIsRejected() {
        try {
            FieldSpec.forJavaType("x", java.util.Map.class, 64);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    // ── every field type roundtrip ──────────────────────────────────

    @Test
    public void allFieldTypesRoundTrip() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 16)) {
            long base = arena.allocate("e1");

            OffHeapCmpAccessor.writeString(l, base, l.indexOf("msisdn"), "84912345678");
            OffHeapCmpAccessor.writeString(l, base, l.indexOf("sessionId"), "sess-42");
            OffHeapCmpAccessor.writeInt(l, base, l.indexOf("menuState"), 3);
            OffHeapCmpAccessor.writeInt(l, base, l.indexOf("retryCount"), -7);
            OffHeapCmpAccessor.writeLong(l, base, l.indexOf("startedAt"), 1_700_000_000_123L);
            OffHeapCmpAccessor.writeBoolean(l, base, l.indexOf("premium"), true);
            OffHeapCmpAccessor.writeDouble(l, base, l.indexOf("balance"), 12.75d);
            OffHeapCmpAccessor.writeBytes(l, base, l.indexOf("token"),
                    new byte[] {1, 2, 3, 4});

            assertEquals("84912345678", OffHeapCmpAccessor.readString(l, base, l.indexOf("msisdn")));
            assertEquals("sess-42", OffHeapCmpAccessor.readString(l, base, l.indexOf("sessionId")));
            assertEquals(3, OffHeapCmpAccessor.readInt(l, base, l.indexOf("menuState")));
            assertEquals(-7, OffHeapCmpAccessor.readInt(l, base, l.indexOf("retryCount")));
            assertEquals(1_700_000_000_123L,
                    OffHeapCmpAccessor.readLong(l, base, l.indexOf("startedAt")));
            assertTrue(OffHeapCmpAccessor.readBoolean(l, base, l.indexOf("premium")));
            assertEquals(12.75d, OffHeapCmpAccessor.readDouble(l, base, l.indexOf("balance")), 0.0);
            assertArrayEquals(new byte[] {1, 2, 3, 4},
                    OffHeapCmpAccessor.readBytes(l, base, l.indexOf("token")));
        }
    }

    @Test
    public void genericNameBasedAccessorsMatchTypedOnes() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            long base = arena.allocate("e1");
            OffHeapCmpAccessor.write(l, base, "msisdn", "111");
            OffHeapCmpAccessor.write(l, base, "menuState", 9);
            assertEquals("111", OffHeapCmpAccessor.read(l, base, "msisdn"));
            assertEquals(9, OffHeapCmpAccessor.read(l, base, "menuState"));
        }
    }

    // ── null / empty / truncation ───────────────────────────────────

    @Test
    public void nullAndEmptyStringsAreDistinct() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            long base = arena.allocate("e1");
            int idx = l.indexOf("msisdn");

            assertNull("fresh field must read null", OffHeapCmpAccessor.readString(l, base, idx));
            OffHeapCmpAccessor.writeString(l, base, idx, "");
            assertEquals("", OffHeapCmpAccessor.readString(l, base, idx));
            OffHeapCmpAccessor.writeString(l, base, idx, "abc");
            OffHeapCmpAccessor.writeString(l, base, idx, null);
            assertNull("null overwrite must stick", OffHeapCmpAccessor.readString(l, base, idx));
        }
    }

    @Test
    public void nullBytesSupported() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            long base = arena.allocate("e1");
            int idx = l.indexOf("token");
            assertNull(OffHeapCmpAccessor.readBytes(l, base, idx));
            OffHeapCmpAccessor.writeBytes(l, base, idx, new byte[0]);
            assertArrayEquals(new byte[0], OffHeapCmpAccessor.readBytes(l, base, idx));
            OffHeapCmpAccessor.writeBytes(l, base, idx, null);
            assertNull(OffHeapCmpAccessor.readBytes(l, base, idx));
        }
    }

    @Test
    public void oversizedValuesAreTruncatedToFieldBudget() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            long base = arena.allocate("e1");
            String longMsisdn = "0123456789012345678901234567890123456789"; // 40 > budget 20
            OffHeapCmpAccessor.writeString(l, base, l.indexOf("msisdn"), longMsisdn);
            assertEquals(longMsisdn.substring(0, 20),
                    OffHeapCmpAccessor.readString(l, base, l.indexOf("msisdn")));

            byte[] bigToken = new byte[64]; // budget 16
            for (int i = 0; i < bigToken.length; i++) bigToken[i] = (byte) i;
            OffHeapCmpAccessor.writeBytes(l, base, l.indexOf("token"), bigToken);
            byte[] readBack = OffHeapCmpAccessor.readBytes(l, base, l.indexOf("token"));
            assertEquals(16, readBack.length);
            assertEquals(15, readBack[15]);
        }
    }

    @Test
    public void multiByteUtf8SurvivesRoundTrip() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            long base = arena.allocate("e1");
            String vietnamese = "Trần Nhân";
            OffHeapCmpAccessor.writeString(l, base, l.indexOf("sessionId"), vietnamese);
            assertEquals(vietnamese,
                    OffHeapCmpAccessor.readString(l, base, l.indexOf("sessionId")));
        }
    }

    // ── unbound guards ──────────────────────────────────────────────

    @Test
    public void unboundBaseReadsDefaultsAndWritesAreNoOps() {
        OffHeapLayout l = layout();
        assertNull(OffHeapCmpAccessor.readString(l, 0L, 0));
        assertEquals(0, OffHeapCmpAccessor.readInt(l, 0L, l.indexOf("menuState")));
        assertEquals(0L, OffHeapCmpAccessor.readLong(l, 0L, l.indexOf("startedAt")));
        assertFalse(OffHeapCmpAccessor.readBoolean(l, 0L, l.indexOf("premium")));
        assertNull(OffHeapCmpAccessor.readBytes(l, 0L, l.indexOf("token")));
        // writes must not crash
        OffHeapCmpAccessor.writeString(l, 0L, 0, "x");
        OffHeapCmpAccessor.writeInt(l, 0L, l.indexOf("menuState"), 1);
    }

    // ── slot lifecycle ──────────────────────────────────────────────

    @Test
    public void allocateIsIdempotentPerEntity() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            assertEquals(arena.allocate("e1"), arena.allocate("e1"));
            assertEquals(1, arena.occupiedCount());
        }
    }

    @Test
    public void freedSlotIsZeroedAndReused() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 4)) {
            long base = arena.allocate("old");
            OffHeapCmpAccessor.writeString(l, base, l.indexOf("msisdn"), "999");
            arena.free("old");
            assertEquals(0L, arena.resolve("old"));

            long reused = arena.allocate("fresh");
            assertEquals("freed slot must be recycled", base, reused);
            assertNull("recycled slot must not leak old state",
                    OffHeapCmpAccessor.readString(l, reused, l.indexOf("msisdn")));
        }
    }

    @Test
    public void freeUnknownEntityIsNoOp() {
        try (OffHeapArena arena = new OffHeapArena("t", layout(), 4)) {
            arena.free("never-existed");
            assertEquals(0, arena.occupiedCount());
        }
    }

    @Test
    public void arenaExhaustionFailsFastAndStaysConsistent() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 2)) {
            arena.allocate("a");
            arena.allocate("b");
            try {
                arena.allocate("c");
                fail("expected exhaustion");
            } catch (IllegalStateException expected) {
                assertTrue(expected.getMessage().contains("exhausted"));
            }
            arena.free("a");
            assertNotEquals(0L, arena.allocate("c")); // freed capacity usable again
        }
    }

    @Test
    public void entitiesAreIsolated() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 8)) {
            long a = arena.allocate("a");
            long b = arena.allocate("b");
            OffHeapCmpAccessor.writeString(l, a, l.indexOf("msisdn"), "AAA");
            OffHeapCmpAccessor.writeString(l, b, l.indexOf("msisdn"), "BBB");
            OffHeapCmpAccessor.writeInt(l, a, l.indexOf("menuState"), 1);
            OffHeapCmpAccessor.writeInt(l, b, l.indexOf("menuState"), 2);
            assertEquals("AAA", OffHeapCmpAccessor.readString(l, a, l.indexOf("msisdn")));
            assertEquals("BBB", OffHeapCmpAccessor.readString(l, b, l.indexOf("msisdn")));
            assertEquals(1, OffHeapCmpAccessor.readInt(l, a, l.indexOf("menuState")));
            assertEquals(2, OffHeapCmpAccessor.readInt(l, b, l.indexOf("menuState")));
        }
    }

    @Test
    public void closedArenaRejectsAllocation() {
        OffHeapArena arena = new OffHeapArena("t", layout(), 4);
        arena.close();
        try {
            arena.allocate("x");
            fail("expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // ok
        }
    }

    // ── compaction ──────────────────────────────────────────────────

    @Test
    public void compactionMovesTopSlotsDownAndRebindsEntities() {
        OffHeapLayout l = layout();
        try (OffHeapArena arena = new OffHeapArena("t", l, 16)) {
            List<String> movedIds = new CopyOnWriteArrayList<>();
            List<Long> movedAddrs = new ArrayList<>();
            arena.setSlotMovedListener((id, addr) -> {
                movedIds.add(id);
                movedAddrs.add(addr);
            });

            arena.allocate("a");
            long bAddr = arena.allocate("b");
            long cAddr = arena.allocate("c");
            OffHeapCmpAccessor.writeString(l, cAddr, l.indexOf("msisdn"), "C-DATA");
            OffHeapCmpAccessor.writeInt(l, cAddr, l.indexOf("menuState"), 42);

            arena.free("b");
            assertEquals(3, arena.highWaterMark());
            assertTrue(arena.fragmentationRatio() > 0);

            int moved = arena.compact();

            assertEquals(1, moved);
            assertEquals(List.of("c"), movedIds);
            assertEquals("c must land in b's old slot", bAddr, (long) movedAddrs.get(0));
            assertEquals(bAddr, arena.resolve("c"));
            assertEquals("data must survive the move", "C-DATA",
                    OffHeapCmpAccessor.readString(l, arena.resolve("c"), l.indexOf("msisdn")));
            assertEquals(42, OffHeapCmpAccessor.readInt(l, arena.resolve("c"),
                    l.indexOf("menuState")));
            assertEquals("high-water mark must shrink", 2, arena.highWaterMark());
            assertEquals(0.0, arena.fragmentationRatio(), 0.0);
        }
    }

    @Test
    public void compactionWithNoGapsIsANoOp() {
        try (OffHeapArena arena = new OffHeapArena("t", layout(), 8)) {
            arena.allocate("a");
            arena.allocate("b");
            assertEquals(0, arena.compact());
        }
    }

    @Test
    public void compactionRetiresTrailingGapsWithoutMoves() {
        try (OffHeapArena arena = new OffHeapArena("t", layout(), 8)) {
            arena.allocate("a");
            arena.allocate("b");
            arena.free("b"); // gap at the top — nothing to move, hwm shrinks
            assertEquals(0, arena.compact());
            assertEquals(1, arena.highWaterMark());
        }
    }

    // ── mmap persistence + recovery ─────────────────────────────────

    @Test
    public void mmapStateSurvivesReopenAndRecovers() {
        OffHeapLayout l = layout();
        Path file = tmp.getRoot().toPath().resolve("cmp/ussd.slab");

        try (OffHeapArena arena = new OffHeapArena("mm", l, 16, file)) {
            long base = arena.allocate("sess-1");
            OffHeapCmpAccessor.writeString(l, base, l.indexOf("msisdn"), "84900000001");
            OffHeapCmpAccessor.writeInt(l, base, l.indexOf("menuState"), 7);
            arena.commit("sess-1");
        }

        try (OffHeapArena reopened = new OffHeapArena("mm", l, 16, file)) {
            List<String> recovered = new ArrayList<>();
            int count = reopened.recover((id, addr) -> recovered.add(id));
            assertEquals(1, count);
            assertEquals(List.of("sess-1"), recovered);
            long base = reopened.resolve("sess-1");
            assertEquals("84900000001",
                    OffHeapCmpAccessor.readString(l, base, l.indexOf("msisdn")));
            assertEquals(7, OffHeapCmpAccessor.readInt(l, base, l.indexOf("menuState")));
            // Recovered arena must keep allocating without clashing.
            long fresh = reopened.allocate("sess-2");
            assertNotEquals(base, fresh);
        }
    }

    @Test
    public void tornWriteIsDetectedByCrcAndFreedOnRecovery() {
        OffHeapLayout l = layout();
        Path file = tmp.getRoot().toPath().resolve("cmp/torn.slab");

        long committedBase;
        try (OffHeapArena arena = new OffHeapArena("mm", l, 16, file)) {
            committedBase = arena.allocate("good");
            OffHeapCmpAccessor.writeString(l, committedBase, l.indexOf("msisdn"), "OK");
            arena.commit("good");

            // Simulate a torn write on a second slot: occupied + garbage,
            // CRC never stamped after the mutation.
            long torn = arena.allocate("torn");
            OffHeapCmpAccessor.writeString(l, torn, l.indexOf("msisdn"), "PARTIAL");
            // no commit → CRC stale
        }

        try (OffHeapArena reopened = new OffHeapArena("mm", l, 16, file)) {
            List<String> recovered = new ArrayList<>();
            reopened.recover((id, addr) -> recovered.add(id));
            assertEquals("only the committed slot may survive",
                    List.of("good"), recovered);
            assertEquals(0L, reopened.resolve("torn"));
        }
    }

    @Test
    public void directArenaRecoverIsNoOp() {
        try (OffHeapArena arena = new OffHeapArena("d", layout(), 4)) {
            assertEquals(0, arena.recover((id, addr) -> fail("no entities expected")));
        }
    }

    @Test
    public void crcDetectsCorruption() {
        OffHeapLayout l = layout();
        Path file = tmp.getRoot().toPath().resolve("cmp/crc.slab");
        try (OffHeapArena arena = new OffHeapArena("mm", l, 4, file)) {
            long base = arena.allocate("e");
            OffHeapCmpAccessor.writeString(l, base, l.indexOf("msisdn"), "123");
            arena.stampCrc(base);
            assertTrue(arena.crcValid(base));
            // flip one payload byte behind the CRC's back
            byte[] evil = "9".getBytes(StandardCharsets.UTF_8);
            OffHeapMemory.copyIn(evil, 0, base + l.payloadOffset(l.indexOf("msisdn")) + 2, 1);
            assertFalse(arena.crcValid(base));
        }
    }
}
