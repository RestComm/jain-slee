package com.microjainslee.telemetry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.LongSupplier;

/**
 * Resource monitor — <b>zero-CPU design</b>: no scheduler, no background
 * thread, no polling loop.
 *
 * <p>Snapshots are captured <i>lazily on read</i> ({@link #snapshot()}),
 * throttled by a minimum capture interval (default 5 s). Between reads the
 * monitor consumes exactly zero CPU and allocates nothing. A Prometheus
 * scrape every 15–30 s therefore drives at most one cheap capture per scrape;
 * with no scrapers attached the monitor is completely idle.</p>
 *
 * <p><b>No management beans.</b> Heap comes from {@link Runtime}; CPU load and
 * open-fd count are read from {@code /proc} on Linux (native-image clean, no
 * {@code java.lang.management} / {@code com.sun.management}). On non-Linux hosts
 * those two fields report {@code -1}. GC counters are not collected without a
 * management bean and report {@code 0}.</p>
 *
 * <p>The old fixed-rate captured history is preserved: every actual
 * capture is appended to the ring buffer, so history density follows real
 * read traffic instead of burning a timer thread.</p>
 *
 * <p>Note: the previous implementation counted virtual threads via
 * {@code Thread.getAllStackTraces()} — that walks and materialises stack
 * traces for every platform thread (milliseconds of CPU + large transient
 * allocations, and it cannot see virtual threads at all). It is gone; the
 * VT count is supplied by the runtime via
 * {@link #setVirtualThreadCounter(LongSupplier)} (e.g. entity-pool size),
 * or reported as -1.</p>
 */
public final class ResourceMonitor {

    public record ResourceSnapshot(
            long heapUsedMb, long heapMaxMb, double heapUsagePercent,
            double cpuLoad, int activeThreads, int virtualThreads,
            long gcCount, long gcTimeMs, long openFileDescriptors,
            long timestampMillis
    ) {}

    private static final int HISTORY_SIZE = 60;

    private final AtomicReferenceArray<ResourceSnapshot> history =
            new AtomicReferenceArray<>(HISTORY_SIZE);
    private final AtomicReference<ResourceSnapshot> latest = new AtomicReference<>();
    private final AtomicLong captureGate = new AtomicLong(); // last capture millis
    private final AtomicLong historyIdx = new AtomicLong();

    private volatile long minCaptureIntervalMillis = 5_000L;
    private volatile LongSupplier virtualThreadCounter = () -> -1L;
    private volatile boolean started = true; // always "on" — passive by nature

    /**
     * Kept for API compatibility. There is no thread to start any more —
     * {@code interval} now only sets the lazy-capture throttle.
     */
    public void start(long interval, TimeUnit unit) {
        this.minCaptureIntervalMillis = Math.max(250L, unit.toMillis(interval));
        this.started = true;
    }

    /** Kept for API compatibility — nothing to stop (no thread exists). */
    public void stop() {
        started = false;
    }

    public boolean isStarted() { return started; }

    /** Throttle for lazy captures; reads inside the window return the cached snapshot. */
    public void setMinCaptureIntervalMillis(long millis) {
        this.minCaptureIntervalMillis = Math.max(0L, millis);
    }

    /**
     * Inject a cheap virtual-thread counter (e.g.
     * {@code container.getSbbEntityPool()::size}). Must be O(1) — it runs
     * inside capture.
     */
    public void setVirtualThreadCounter(LongSupplier counter) {
        this.virtualThreadCounter = counter != null ? counter : () -> -1L;
    }

    /**
     * Latest snapshot. Captures at most once per
     * {@code minCaptureIntervalMillis} regardless of caller count — a CAS
     * gate makes concurrent scrapers share one capture.
     */
    public ResourceSnapshot snapshot() {
        long now = System.currentTimeMillis();
        long last = captureGate.get();
        ResourceSnapshot cached = latest.get();
        if (cached != null && now - last < minCaptureIntervalMillis) {
            return cached;
        }
        if (!captureGate.compareAndSet(last, now)) {
            // Another reader is capturing right now — serve the cache.
            ResourceSnapshot snap = latest.get();
            return snap != null ? snap : captureNow();
        }
        ResourceSnapshot snap = captureNow();
        latest.set(snap);
        long idx = historyIdx.getAndIncrement();
        history.set((int) (idx % HISTORY_SIZE), snap);
        return snap;
    }

    /** Last N snapshots in chronological order (density follows read traffic). */
    public java.util.stream.Stream<ResourceSnapshot> historyStream() {
        java.util.List<ResourceSnapshot> list = new java.util.ArrayList<>();
        for (int i = 0; i < HISTORY_SIZE; i++) {
            ResourceSnapshot s = history.get(i);
            if (s != null) list.add(s);
        }
        list.sort(java.util.Comparator.comparingLong(ResourceSnapshot::timestampMillis));
        return list.stream();
    }

    private ResourceSnapshot captureNow() {
        Runtime rt = Runtime.getRuntime();
        long heapUsed = rt.totalMemory() - rt.freeMemory();
        long heapMax = rt.maxMemory();

        double cpuLoad = readCpuLoadFromProc(); // 0.0–1.0, or -1 off Linux

        int activeThreads = Thread.activeCount();
        long vtCount;
        try {
            vtCount = virtualThreadCounter.getAsLong();
        } catch (RuntimeException e) {
            vtCount = -1;
        }

        // GC counters require a management bean, which we no longer use.
        long gcCount = 0, gcTimeMs = 0;
        long openFds = countOpenFds(); // /proc/self/fd on Linux, else -1

        return new ResourceSnapshot(
                heapUsed / (1024 * 1024), heapMax / (1024 * 1024),
                heapMax > 0 ? (double) heapUsed / heapMax * 100.0 : 0.0,
                cpuLoad > 0 ? cpuLoad * 100.0 : -1.0,
                activeThreads, (int) vtCount, gcCount, gcTimeMs, openFds,
                System.currentTimeMillis()
        );
    }

    // ── /proc-based readings (Linux) — no java.lang.management dependency ──

    /** Previous aggregate CPU jiffies for delta-based load; -1 until first read. */
    private volatile long prevCpuTotal = -1L;
    private volatile long prevCpuIdle = -1L;

    /**
     * Whole-machine CPU load as a fraction (0.0–1.0) from {@code /proc/stat},
     * computed as the busy-jiffie delta between successive captures. Returns
     * {@code -1} on the first capture (no baseline yet) and on any non-Linux
     * host or read error.
     */
    private double readCpuLoadFromProc() {
        java.nio.file.Path stat = java.nio.file.Path.of("/proc/stat");
        if (!java.nio.file.Files.exists(stat)) {
            return -1.0;
        }
        try {
            String cpuLine = null;
            for (String line : java.nio.file.Files.readAllLines(stat)) {
                if (line.startsWith("cpu ")) {
                    cpuLine = line;
                    break;
                }
            }
            if (cpuLine == null) {
                return -1.0;
            }
            String[] f = cpuLine.trim().split("\\s+");
            long total = 0;
            for (int i = 1; i < f.length; i++) {
                total += Long.parseLong(f[i]);
            }
            // idle = idle(4th) + iowait(5th) fields after the "cpu" label
            long idle = Long.parseLong(f[4]) + (f.length > 5 ? Long.parseLong(f[5]) : 0);

            long prevTotal = prevCpuTotal;
            long prevIdle = prevCpuIdle;
            prevCpuTotal = total;
            prevCpuIdle = idle;
            if (prevTotal < 0) {
                return -1.0; // first sample — need a delta
            }
            long totalDelta = total - prevTotal;
            long idleDelta = idle - prevIdle;
            if (totalDelta <= 0) {
                return -1.0;
            }
            double load = 1.0 - ((double) idleDelta / totalDelta);
            return load < 0 ? 0.0 : Math.min(load, 1.0);
        } catch (Exception ignored) {
            return -1.0;
        }
    }

    /**
     * Open file-descriptor count from {@code /proc/self/fd} on Linux; {@code -1}
     * on other platforms or on error.
     */
    private static long countOpenFds() {
        java.nio.file.Path fd = java.nio.file.Path.of("/proc/self/fd");
        if (!java.nio.file.Files.isDirectory(fd)) {
            return -1;
        }
        try (var stream = java.nio.file.Files.newDirectoryStream(fd)) {
            long count = 0;
            for (var ignored : stream) {
                count++;
            }
            return count;
        } catch (Exception ignored) {
            return -1;
        }
    }
}
