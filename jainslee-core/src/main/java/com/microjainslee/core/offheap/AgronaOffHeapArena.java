/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import org.agrona.concurrent.UnsafeBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fixed-size-slot off-heap arena backed by an Agrona {@link UnsafeBuffer}
 * (P2). Same API as {@link OffHeapArena} but uses Agrona's buffer access
 * for 4–6× faster typed reads/writes and zero GC pressure.
 *
 * <p>Backend: {@code DIRECT} only (DirectByteBuffer wrapped by
 * {@code UnsafeBuffer}). No MMAP support in this implementation.</p>
 *
 * <p>Slot addressing: {@code base = arena.addressOffset() + slotIdx * slotSize}.</p>
 *
 * <p>Compaction (§7): when free slots exceed the threshold, occupied
 * slots at the top are moved down into the gaps; the entity's new base
 * address is published to the {@link SlotMovedListener} so the entity
 * pool can rebind live SBB objects.</p>
 */
public final class AgronaOffHeapArena implements OffHeapSlotArena {

    private static final Logger LOG = LogManager.getLogger(AgronaOffHeapArena.class);

    /** Notified when compaction moves an entity's slot. */
    @FunctionalInterface
    public interface SlotMovedListener {
        void onSlotMoved(String entityId, long newBaseAddr);
    }

    private final String name;
    private final OffHeapLayout layout;
    private final int maxSlots;
    private final int slotSize;

    private final UnsafeBuffer arena;
    private final long baseAddr;
    private final Map<String, Integer> slotMap = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Integer> freeList = new ConcurrentLinkedQueue<>();
    private final AtomicInteger nextSlotIndex = new AtomicInteger();
    private volatile SlotMovedListener slotMovedListener;
    private volatile boolean closed;

    /**
     * Create a DIRECT arena backed by an Agrona {@link UnsafeBuffer}.
     *
     * @param name     arena name (for logging)
     * @param layout   compiled slot layout for one SBB type
     * @param maxSlots maximum number of entity slots
     */
    public AgronaOffHeapArena(String name, OffHeapLayout layout, int maxSlots) {
        long capacity = (long) layout.slotSize() * maxSlots;
        if (capacity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Arena capacity " + capacity
                    + " exceeds 2 GB addressability (slotSize=" + layout.slotSize()
                    + " × maxSlots=" + maxSlots + ") — split per SBB type or reduce maxSlots");
        }
        this.name = name;
        this.layout = layout;
        this.maxSlots = maxSlots;
        this.slotSize = layout.slotSize();
        ByteBuffer buf = ByteBuffer.allocateDirect((int) capacity);
        this.arena = new UnsafeBuffer(buf);
        this.baseAddr = arena.addressOffset();
        LOG.info("[offheap-agrona:{}] arena ready: {} slots × {}B = {} MB (agrona-direct)",
                name, maxSlots, layout.slotSize(), capacity / (1024 * 1024));
    }

    public OffHeapLayout layout() { return layout; }
    public String name() { return name; }
    public int maxSlots() { return maxSlots; }
    public int occupiedCount() { return slotMap.size(); }
    public int freeListSize() { return freeList.size(); }
    public int highWaterMark() { return nextSlotIndex.get(); }
    public void setSlotMovedListener(SlotMovedListener listener) {
        this.slotMovedListener = listener;
    }

    /** Free slots as a fraction of allocated slots (compaction trigger input). */
    public double fragmentationRatio() {
        int hwm = nextSlotIndex.get();
        return hwm == 0 ? 0.0 : (double) freeList.size() / hwm;
    }

    // ── allocate / free / resolve ───────────────────────────────────

    /** Allocate (or return the existing) slot for {@code entityId}. */
    public synchronized long allocate(String entityId) {
        checkOpen();
        Integer existing = slotMap.get(entityId);
        if (existing != null) {
            return slotAddr(existing);
        }
        Integer idx = freeList.poll();
        if (idx == null) {
            idx = nextSlotIndex.getAndIncrement();
            if (idx >= maxSlots) {
                nextSlotIndex.decrementAndGet();
                throw new IllegalStateException("[offheap-agrona:" + name + "] arena exhausted ("
                        + maxSlots + " slots) — raise maxSlots or expire entities");
            }
        }
        long base = slotAddr(idx);
        initSlot(base, entityId);
        slotMap.put(entityId, idx);
        return base;
    }

    /** Release the entity's slot back to the free list (zero-filled). */
    public synchronized void free(String entityId) {
        Integer idx = slotMap.remove(entityId);
        if (idx == null) {
            return;
        }
        OffHeapMemory.zero(slotAddr(idx), slotSize);
        freeList.add(idx);
    }

    /** Base address for a live entity, or 0 when absent. */
    public long resolve(String entityId) {
        Integer idx = slotMap.get(entityId);
        return idx == null ? 0L : slotAddr(idx);
    }

    // ── compaction (§7) ─────────────────────────────────────────────

    /**
     * Compact the arena: move top occupied slots down into free gaps,
     * update the slot map, publish new addresses, lower the high-water
     * mark. Returns the number of moved entities.
     */
    public synchronized int compact() {
        checkOpen();
        if (freeList.isEmpty()) {
            return 0;
        }
        java.util.TreeSet<Integer> gaps = new java.util.TreeSet<>(freeList);
        Map<Integer, String> byIndex = new ConcurrentHashMap<>();
        slotMap.forEach((id, idx) -> byIndex.put(idx, id));

        int moved = 0;
        int hwm = nextSlotIndex.get();
        for (int top = hwm - 1; top >= 0 && !gaps.isEmpty(); top--) {
            Integer gap = gaps.first();
            if (gap >= top) {
                break;
            }
            String entityId = byIndex.get(top);
            if (entityId == null) {
                gaps.remove(top);
                continue;
            }
            gaps.pollFirst();
            long from = slotAddr(top);
            long to = slotAddr(gap);
            OffHeapMemory.copy(from, to, slotSize);
            OffHeapMemory.zero(from, slotSize);
            slotMap.put(entityId, gap);
            byIndex.put(gap, entityId);
            byIndex.remove(top);
            gaps.add(top);
            moved++;
            SlotMovedListener listener = this.slotMovedListener;
            if (listener != null) {
                listener.onSlotMoved(entityId, to);
            }
        }
        int newHwm = byIndex.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1) + 1;
        nextSlotIndex.set(newHwm);
        freeList.clear();
        gaps.stream().filter(g -> g < newHwm).forEach(freeList::add);
        if (moved > 0) {
            LOG.info("[offheap-agrona:{}] compacted: moved={} hwm {}→{} free={}",
                    name, moved, hwm, newHwm, freeList.size());
        }
        return moved;
    }

    @Override
    public synchronized void close() {
        closed = true;
        slotMap.clear();
        freeList.clear();
        // DirectByteBuffer memory is released by GC; explicit cleaner
        // invocation is deliberately avoided (unsafe if any bound SBB
        // still holds the base address).
    }

    // ── slot internals ──────────────────────────────────────────────

    private long slotAddr(int idx) {
        return baseAddr + (long) idx * slotSize;
    }

    /** Convert a raw base address to a zero-based offset within the arena. */
    private int arenaOffset(long base) {
        return (int) (base - baseAddr);
    }

    private void initSlot(long base, String entityId) {
        int off = arenaOffset(base);
        OffHeapMemory.zero(base, slotSize);
        arena.putInt(off + OffHeapLayout.OFF_MAGIC, OffHeapLayout.MAGIC);
        arena.putShort(off + OffHeapLayout.OFF_FIELD_COUNT, (short) layout.fieldCount());
        for (int i = 0; i < layout.fieldCount(); i++) {
            int dirOff = off + OffHeapLayout.OFF_DIRECTORY + i * OffHeapLayout.DIR_ENTRY_SIZE;
            arena.putShort(dirOff, layout.nameHash(i));
            arena.putShort(dirOff + 2, (short) layout.field(i).type());
            int type = layout.field(i).type();
            if (type == OffHeapLayout.T_LONG || type == OffHeapLayout.T_DOUBLE) {
                arena.putInt(dirOff + 4, layout.payloadOffset(i));
            } else {
                arena.putInt(dirOff + 4, 0);
            }
        }
        writeEntityId(base, entityId);
        // Commit protocol: occupied flag last (§8).
        arena.putShort(off + OffHeapLayout.OFF_FLAGS, OffHeapLayout.FLAG_OCCUPIED);
    }

    private void writeEntityId(long base, String entityId) {
        byte[] bytes = entityId.getBytes(StandardCharsets.UTF_8);
        int len = Math.min(bytes.length, OffHeapLayout.ENTITY_ID_RESERVE - 2);
        int off = arenaOffset(base + layout.entityIdOffset());
        arena.putShort(off, (short) len);
        arena.putBytes(off + 2, bytes, 0, len);
    }

    @SuppressWarnings("unused")
    private String readEntityId(long base) {
        int off = arenaOffset(base + layout.entityIdOffset());
        int len = arena.getShort(off) & 0xFFFF;
        if (len == 0 || len > OffHeapLayout.ENTITY_ID_RESERVE - 2) {
            return null;
        }
        byte[] buf = new byte[len];
        arena.getBytes(off + 2, buf, 0, len);
        return new String(buf, StandardCharsets.UTF_8);
    }

    private void checkOpen() {
        if (closed) {
            throw new IllegalStateException("[offheap-agrona:" + name + "] arena is closed");
        }
    }
}

