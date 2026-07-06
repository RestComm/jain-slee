/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.prometheus.collab;

import java.util.List;

/**
 * Store for Prometheus counters and gauges registered by application SBBs
 * via the RA command port.
 *
 * <p>Implementations are thread-safe. The default {@link InMemory} uses
 * a {@link java.util.concurrent.ConcurrentHashMap} for lock-free access
 * during scrape.
 */
public interface PrometheusMetricsStore {

    /** Register a counter metric. Idempotent. */
    void trackCounter(String name, String... tags);

    /** Increment a counter by {@code n}. Implicitly registers if new. */
    void incrementCounter(String name, long n, String... tags);

    /** Set a gauge to an absolute value. Implicitly registers if new. */
    void setGauge(String name, double value, String... tags);

    /** Return all registered metric entries. */
    List<MetricEntry> entries();

    /** Serialise to OpenMetrics text format. */
    String toPrometheusText();

    /** Total number of registered metric lines. */
    int count();

    // ── inner types ────────────────────────────────────────────────

    interface MetricEntry {
        String name();
        String help();
        String type();
        String prometheusLine();
    }

    // ── default implementation ─────────────────────────────────────

    final class InMemory implements PrometheusMetricsStore {

        private final java.util.concurrent.ConcurrentHashMap<String, MetricEntry> metrics =
                new java.util.concurrent.ConcurrentHashMap<>();
        private final java.util.concurrent.atomic.AtomicLong counterSeq =
                new java.util.concurrent.atomic.AtomicLong(1);

        @Override
        public void trackCounter(String name, String... tags) {
            String key = buildKey(name, tags);
            metrics.computeIfAbsent(key, k -> new CounterEntry(
                    Long.toString(counterSeq.getAndIncrement()), name, tags));
        }

        @Override
        public void incrementCounter(String name, long n, String... tags) {
            String key = buildKey(name, tags);
            MetricEntry existing = metrics.get(key);
            if (existing instanceof CounterEntry ce) {
                ce.adder.add(n);
            } else {
                CounterEntry ce = new CounterEntry(
                        Long.toString(counterSeq.getAndIncrement()), name, tags);
                ce.adder.add(n);
                metrics.put(key, ce);
            }
        }


        @Override
        public void setGauge(String name, double value, String... tags) {
            String key = buildKey(name, tags);
            GaugeEntry ge = new GaugeEntry(
                    Long.toString(counterSeq.getAndIncrement()), name, value, tags);
            metrics.put(key, ge);
        }

        @Override
        public List<MetricEntry> entries() {
            return List.copyOf(metrics.values());
        }

        @Override
        public int count() {
            return metrics.size();
        }

        @Override
        public String toPrometheusText() {
            StringBuilder sb = new StringBuilder(4096);
            for (MetricEntry e : metrics.values()) {
                sb.append(e.help()).append('\n');
                sb.append(e.type()).append('\n');
                sb.append(e.prometheusLine()).append('\n');
            }
            sb.append("# HELP micro_jainslee_up Always 1 when the RA is running.\n");
            sb.append("# TYPE micro_jainslee_up gauge\n");
            sb.append("micro_jainslee_up 1\n");
            return sb.toString();
        }

        // ── key / tag helpers ───────────────────────────────────

        static String buildKey(String name, String... tags) {
            StringBuilder sb = new StringBuilder(name);
            for (int i = 0; i < tags.length; i += 2) {
                sb.append(';').append(tags[i]).append('=');
                if (i + 1 < tags.length) {
                    sb.append(tags[i + 1]);
                }
            }
            return sb.toString();
        }

        static String buildTagString(String... tags) {
            if (tags == null || tags.length == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder("{");
            for (int i = 0; i < tags.length; i += 2) {
                if (i > 0) sb.append(',');
                sb.append(tags[i]).append("=\"");
                if (i + 1 < tags.length) {
                    sb.append(escapeLabelValue(tags[i + 1]));
                }
                sb.append('"');
            }
            sb.append('}');
            return sb.toString();
        }

        static String escapeLabelValue(String value) {
            return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");
        }

        // ── entry impls ─────────────────────────────────────────

        private static final class CounterEntry implements MetricEntry {
            private final String id, name, tagString;
            private final String[] tags;
            final java.util.concurrent.atomic.LongAdder adder =
                    new java.util.concurrent.atomic.LongAdder();

            CounterEntry(String id, String name, String... tags) {
                this.id = id;
                this.name = name;
                this.tags = tags.clone();
                this.tagString = buildTagString(tags);
            }

            @Override public String name() { return name; }
            @Override public String help() {
                return "# HELP " + name + " Counter metric (id=" + id + ").";
            }
            @Override public String type() {
                return "# TYPE " + name + " counter";
            }
            @Override public String prometheusLine() {
                return name + tagString + " " + adder.sum();
            }
        }

        private static final class GaugeEntry implements MetricEntry {
            private final String id, name, tagString;
            private final String[] tags;
            private volatile double value;

            GaugeEntry(String id, String name, double value, String... tags) {
                this.id = id;
                this.name = name;
                this.tags = tags.clone();
                this.tagString = buildTagString(tags);
                this.value = value;
            }

            @Override public String name() { return name; }
            @Override public String help() {
                return "# HELP " + name + " Gauge metric (id=" + id + ").";
            }
            @Override public String type() {
                return "# TYPE " + name + " gauge";
            }
            @Override public String prometheusLine() {
                return name + tagString + " " + value;
            }
        }
    }
}
