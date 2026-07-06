package com.microjainslee.telemetry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Resource monitor — single daemon VT for periodic snapshots (30s).
 * Ring buffer for 30-minute history. Snapshots are cached (AtomicReference).
 */
public final class ResourceMonitor {

    public record ResourceSnapshot(
            long heapUsedMb, long heapMaxMb, double heapUsagePercent,
            double cpuLoad, int activeThreads, int virtualThreads,
            long gcCount, long gcTimeMs, long openFileDescriptors,
            long timestampMillis
    ) {}

    private static final int HISTORY_SIZE = 60; // 30 minutes at 30s intervals
    private final AtomicReferenceArray<ResourceSnapshot> history =
            new AtomicReferenceArray<>(HISTORY_SIZE);

    private final AtomicReference<ResourceSnapshot> latest = new AtomicReference<>();
    private ScheduledExecutorService scheduler;
    private volatile boolean started;

    /** Start periodic collection. Single daemon VT, no polling on read. */
    public void start(long interval, TimeUnit unit) {
        if (started) return;
        started = true;
        ThreadFactory tf = Thread.ofVirtual().name("telemetry-resmon").factory();
        scheduler = Executors.newSingleThreadScheduledExecutor(tf);
        scheduler.scheduleAtFixedRate(this::capture, 0, interval, unit);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
        started = false;
    }

    public boolean isStarted() { return started; }

    /** Latest snapshot (no allocation — AtomicReference read). */
    public ResourceSnapshot snapshot() {
        ResourceSnapshot snap = latest.get();
        if (snap != null) return snap;
        return captureNow();
    }

    /** Last N snapshots in chronological order. */
    public java.util.stream.Stream<ResourceSnapshot> historyStream() {
        java.util.List<ResourceSnapshot> list = new java.util.ArrayList<>();
        for (int i = 0; i < HISTORY_SIZE; i++) {
            ResourceSnapshot s = history.get(i);
            if (s != null) list.add(s);
        }
        return list.stream();
    }

    private int historyIdx;

    private void capture() {
        ResourceSnapshot snap = captureNow();
        int idx = historyIdx;
        history.set(idx % HISTORY_SIZE, snap);
        historyIdx = idx + 1;
        latest.set(snap);
    }

    private ResourceSnapshot captureNow() {
        Runtime rt = Runtime.getRuntime();
        long heapUsed = rt.totalMemory() - rt.freeMemory();
        long heapMax = rt.maxMemory();

        double cpuLoad = -1.0;
        try {
            var bean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            if (bean instanceof com.sun.management.OperatingSystemMXBean osBean) {
                cpuLoad = osBean.getCpuLoad();
                if (cpuLoad < 0) cpuLoad = osBean.getProcessCpuLoad();
            }
        } catch (Exception ignored) { }

        int activeThreads = Thread.activeCount();
        long vtCount = Thread.getAllStackTraces().keySet().stream()
                .filter(Thread::isVirtual).count();

        long gcCount = 0, gcTimeMs = 0;
        try {
            for (var gcBean : java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()) {
                gcCount += gcBean.getCollectionCount();
                gcTimeMs += gcBean.getCollectionTime();
            }
        } catch (Exception ignored) { }

        long openFds = -1;
        try {
            openFds = java.lang.management.ManagementFactory.getOperatingSystemMXBean()
                    instanceof com.sun.management.UnixOperatingSystemMXBean unix
                    ? unix.getOpenFileDescriptorCount() : -1;
        } catch (Exception ignored) { }

        return new ResourceSnapshot(
                heapUsed / (1024 * 1024), heapMax / (1024 * 1024),
                heapMax > 0 ? (double) heapUsed / heapMax * 100.0 : 0.0,
                cpuLoad > 0 ? cpuLoad * 100.0 : -1.0,
                activeThreads, (int) vtCount, gcCount, gcTimeMs, openFds,
                System.currentTimeMillis()
        );
    }
}
