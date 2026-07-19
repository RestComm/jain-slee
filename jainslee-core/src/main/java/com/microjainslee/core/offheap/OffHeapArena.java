/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ObjLongConsumer;
import java.util.zip.CRC32;

/**
 * Fixed-size-slot off-heap arena — one per SBB type (design doc §6).
 *
 * <p>Backends: {@code DIRECT} (DirectByteBuffer, fastest, volatile) and
 * {@code MMAP} (memory-mapped file, survives restart; CRC32 + flag-last
 * commit protocol per §8). Slot addressing is pure arithmetic:
 * {@code base = arenaBase + slotIdx * slotSize}.</p>
 *
 * <p>Compaction (§7): when free slots exceed the threshold, occupied
 * slots at the top are moved down into the gaps; the entity's new base
 * address is published to the {@link SlotMovedListener} so the entity
 * pool can rebind live SBB objects. Run compaction from a quiesced
 * context (the autonomous guardian) — CMP access is single-threaded per
 * entity, but the mover must not race the entity's own thread.</p>
 */
public final class OffHeapArena implements OffHeapSlotArena {

    private static final Logger LOG = LogManager.getLogger(OffHeapArena.class);

    /** Notified when compaction moves an entity's slot. */
    @FunctionalInterface
    public interface SlotMovedListener {
        void onSlotMoved(String entityId, long newBaseAddr);
    }

    private final String name;
    private final OffHeapLayout layout;
    private final int maxSlots;
    private final boolean mmap;
    private final Path filePath;

    private final ByteBuffer arena;      // strong ref — keeps memory alive
    private final long baseAddr;
    private final Map<String, Integer> slotMap = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Integer> freeList = new ConcurrentLinkedQueue<>();
    private final AtomicInteger nextSlotIndex = new AtomicInteger();
    private volatile SlotMovedListener slotMovedListener;
    private volatile boolean closed;

    public OffHeapArena(String name, OffHeapLayout layout, int maxSlots) {
        this(name, layout, maxSlots, null);
    }

    /** {@code filePath != null} → MMAP mode with crash recovery. */
    public OffHeapArena(String name, OffHeapLayout layout, int maxSlots, Path filePath) {
        long capacity = (long) layout.slotSize() * maxSlots;
        if (capacity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Arena capacity " + capacity
                    + " exceeds 2 GB addressability (slotSize=" + layout.slotSize()
                    + " × maxSlots=" + maxSlots + ") — split per SBB type or reduce maxSlots");
        }
        this.name = name;
        this.layout = layout;
        this.maxSlots = maxSlots;
        this.mmap = filePath != null;
        this.filePath = filePath;
        if (mmap) {
            try {
                Files.createDirectories(filePath.toAbsolutePath().getParent());
                try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "rw");
                     FileChannel channel = raf.getChannel()) {
                    this.arena = channel.map(FileChannel.MapMode.READ_WRITE, 0, capacity);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to map off-heap file " + filePath, e);
            }
        } else {
            this.arena = ByteBuffer.allocateDirect((int) capacity);
        }
        this.baseAddr = OffHeapMemory.addressOf(arena);
        LOG.info("[offheap:{}] arena ready: {} slots × {}B = {} MB ({})",
                name, maxSlots, layout.slotSize(), capacity / (1024 * 1024),
                mmap ? "mmap:" + filePath : "direct");
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
                throw new IllegalStateException("[offheap:" + name + "] arena exhausted ("
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
        OffHeapMemory.zero(slotAddr(idx), layout.slotSize());
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
        // slotIdx → entityId reverse index for the move loop
        Map<Integer, String> byIndex = new ConcurrentHashMap<>();
        slotMap.forEach((id, idx) -> byIndex.put(idx, id));

        int moved = 0;
        int hwm = nextSlotIndex.get();
        for (int top = hwm - 1; top >= 0 && !gaps.isEmpty(); top--) {
            Integer gap = gaps.first();
            if (gap >= top) {
                break; // all gaps are above the highest occupied slot
            }
            String entityId = byIndex.get(top);
            if (entityId == null) {
                gaps.remove(top); // top itself is a gap — retire it
                continue;
            }
            gaps.pollFirst();
            long from = slotAddr(top);
            long to = slotAddr(gap);
            OffHeapMemory.copy(from, to, layout.slotSize());
            OffHeapMemory.zero(from, layout.slotSize());
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
        // New high-water mark = highest occupied + 1; everything above is free.
        int newHwm = byIndex.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1) + 1;
        nextSlotIndex.set(newHwm);
        freeList.clear();
        gaps.stream().filter(g -> g < newHwm).forEach(freeList::add);
        if (moved > 0) {
            LOG.info("[offheap:{}] compacted: moved={} hwm {}→{} free={}",
                    name, moved, hwm, newHwm, freeList.size());
        }
        return moved;
    }

    // ── mmap recovery (§8) ──────────────────────────────────────────

    /**
     * Scan an mmap arena for valid occupied slots and rebuild
     * {@code slotMap}/{@code freeList}. For each recovered entity the
     * consumer receives {@code (entityId, baseAddr)}. Torn writes
     * (occupied flag set but CRC invalid) are freed per the commit
     * protocol. No-op for DIRECT arenas.
     */
    public synchronized int recover(ObjLongConsumer<String> recoveredEntityConsumer) {
        if (!mmap) {
            return 0;
        }
        int recovered = 0;
        int highest = -1;
        for (int idx = 0; idx < maxSlots; idx++) {
            long base = slotAddr(idx);
            if (OffHeapMemory.getInt(base + OffHeapLayout.OFF_MAGIC) != OffHeapLayout.MAGIC) {
                continue;
            }
            boolean occupied = (OffHeapMemory.getShort(base + OffHeapLayout.OFF_FLAGS)
                    & OffHeapLayout.FLAG_OCCUPIED) != 0;
            if (!occupied) {
                continue;
            }
            if (!crcValid(base)) {
                LOG.warn("[offheap:{}] slot {} torn write (CRC mismatch) — freed", name, idx);
                OffHeapMemory.zero(base, layout.slotSize());
                continue;
            }
            String entityId = readEntityId(base);
            if (entityId == null || entityId.isEmpty()) {
                OffHeapMemory.zero(base, layout.slotSize());
                continue;
            }
            slotMap.put(entityId, idx);
            highest = Math.max(highest, idx);
            recovered++;
            if (recoveredEntityConsumer != null) {
                recoveredEntityConsumer.accept(entityId, base);
            }
        }
        nextSlotIndex.set(highest + 1);
        freeList.clear();
        for (int idx = 0; idx <= highest; idx++) {
            long base = slotAddr(idx);
            boolean occupied = OffHeapMemory.getInt(base) == OffHeapLayout.MAGIC
                    && (OffHeapMemory.getShort(base + OffHeapLayout.OFF_FLAGS)
                            & OffHeapLayout.FLAG_OCCUPIED) != 0;
            if (!occupied) {
                freeList.add(idx);
            }
        }
        LOG.info("[offheap:{}] recovery: {} entities, hwm={}, free={}",
                name, recovered, nextSlotIndex.get(), freeList.size());
        return recovered;
    }

    /**
     * MMAP commit: stamp the CRC over the slot content, then force pages
     * to disk. Call from {@code sbbStore()} for critical state (§8).
     * No-op for DIRECT arenas.
     */
    public void commit(String entityId) {
        if (!mmap) {
            return;
        }
        long base = resolve(entityId);
        if (base == 0L) {
            return;
        }
        stampCrc(base);
        if (arena instanceof MappedByteBuffer mapped) {
            mapped.force();
        }
    }

    @Override
    public synchronized void close() {
        closed = true;
        slotMap.clear();
        freeList.clear();
        if (arena instanceof MappedByteBuffer mapped) {
            mapped.force();
        }
        // DirectByteBuffer memory is released by GC of `arena`; explicit
        // cleaner invocation is deliberately avoided (unsafe if any bound
        // SBB still holds the base address).
    }

    // ── slot internals ──────────────────────────────────────────────

    private long slotAddr(int idx) {
        return baseAddr + (long) idx * layout.slotSize();
    }

    private void initSlot(long base, String entityId) {
        OffHeapMemory.zero(base, layout.slotSize());
        OffHeapMemory.putInt(base + OffHeapLayout.OFF_MAGIC, OffHeapLayout.MAGIC);
        OffHeapMemory.putShort(base + OffHeapLayout.OFF_FIELD_COUNT,
                (short) layout.fieldCount());
        for (int i = 0; i < layout.fieldCount(); i++) {
            long dir = layout.dirEntryAddr(base, i);
            OffHeapMemory.putShort(dir, layout.nameHash(i));
            OffHeapMemory.putShort(dir + 2, (short) layout.field(i).type());
            // var-length fields start as null (offset 0); longs/doubles get
            // their fixed payload offset immediately (primitives can't be null)
            int type = layout.field(i).type();
            if (type == OffHeapLayout.T_LONG || type == OffHeapLayout.T_DOUBLE) {
                OffHeapMemory.putInt(dir + 4, layout.payloadOffset(i));
            } else {
                OffHeapMemory.putInt(dir + 4, 0);
            }
        }
        writeEntityId(base, entityId);
        if (mmap) {
            stampCrc(base);
        }
        // Commit protocol: occupied flag last (§8).
        OffHeapMemory.putShort(base + OffHeapLayout.OFF_FLAGS, OffHeapLayout.FLAG_OCCUPIED);
        if (mmap) {
            stampCrc(base); // flags participate in the CRC
        }
    }

    private void writeEntityId(long base, String entityId) {
        byte[] bytes = entityId.getBytes(StandardCharsets.UTF_8);
        int len = Math.min(bytes.length, OffHeapLayout.ENTITY_ID_RESERVE - 2);
        long addr = base + layout.entityIdOffset();
        OffHeapMemory.putShort(addr, (short) len);
        OffHeapMemory.copyIn(bytes, 0, addr + 2, len);
    }

    private String readEntityId(long base) {
        long addr = base + layout.entityIdOffset();
        int len = OffHeapMemory.getShort(addr) & 0xFFFF;
        if (len == 0 || len > OffHeapLayout.ENTITY_ID_RESERVE - 2) {
            return null;
        }
        byte[] buf = new byte[len];
        OffHeapMemory.copyOut(addr + 2, buf, 0, len);
        return new String(buf, StandardCharsets.UTF_8);
    }

    /** CRC32 over [0, crcOffset) — visible for tests. */
    void stampCrc(long base) {
        OffHeapMemory.putInt(base + layout.crcOffset(), computeCrc(base));
    }

    boolean crcValid(long base) {
        return OffHeapMemory.getInt(base + layout.crcOffset()) == computeCrc(base);
    }

    private int computeCrc(long base) {
        int len = layout.crcOffset();
        byte[] buf = new byte[len];
        OffHeapMemory.copyOut(base, buf, 0, len);
        CRC32 crc = new CRC32();
        crc.update(buf, 0, len);
        return (int) crc.getValue();
    }

    private void checkOpen() {
        if (closed) {
            throw new IllegalStateException("[offheap:" + name + "] arena is closed");
        }
    }
}
