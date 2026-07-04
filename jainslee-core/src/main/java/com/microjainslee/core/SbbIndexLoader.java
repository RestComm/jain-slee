/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads {@code META-INF/microjainslee/sbb-index.properties} produced by APT.
 *
 * <p>GOAL 3 — also supports programmatic SBB registration via
 * {@link #registerSbb(Class)}. Programmatic entries take priority
 * over descriptor-based entries from the classpath index.
 */
public final class SbbIndexLoader {

    public static final String INDEX_RESOURCE = "META-INF/microjainslee/sbb-index.properties";

    /**
     * GOAL 3 — programmatic deployable-unit registrations keyed by
     * their class name. Entries here are prepended to the descriptor-based
     * list returned by {@link #load(ClassLoader)}.
     */
    private static final ConcurrentHashMap<String, DeployableUnitIndexEntry> programmaticDus =
            new ConcurrentHashMap<>();

    // ───────────────────────────────────────────────────────────────
    // P0 — event-to-SBB index with immutable frozen copy for zero-lock reads
    // ───────────────────────────────────────────────────────────────

    /**
     * Mutable build-time index: event class name → list of SBB class names
     * that handle it. Populated during {@link #build()} and frozen into
     * {@link #frozenIndex} for zero-lock reads at runtime.
     */
    private static final ConcurrentHashMap<String, List<String>> eventIndex =
            new ConcurrentHashMap<>();

    /**
     * Immutable frozen copy of {@link #eventIndex} produced by
     * {@link #freeze()}. Reads on this map are lock-free since
     * {@link Map#copyOf} guarantees an unmodifiable snapshot.
     * When {@code null}, lookup methods fall back to {@link #eventIndex}.
     */
    private static volatile Map<String, List<String>> frozenIndex;

    private SbbIndexLoader() {
    }

    public static SbbIndex load(ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader is required");
        }
        SbbIndex descriptorIndex;
        InputStream in = classLoader.getResourceAsStream(INDEX_RESOURCE);
        if (in == null) {
            descriptorIndex = SbbIndex.empty();
        } else {
            try {
                Properties props = new Properties();
                props.load(in);
                descriptorIndex = parse(props);
            } finally {
                in.close();
            }
        }
        // GOAL 3 — merge programmatic registrations (higher priority first).
        return mergeProgrammatic(descriptorIndex);
    }

    /**
     * GOAL 3 — register an SBB class programmatically. Creates a synthetic
     * {@link DeployableUnitIndexEntry} that will be discovered by the container
     * during {@code autoDeployFromClasspathIndex()}.
     *
     * <p>Programmatic registrations <strong>take priority</strong> over
     * descriptor-based entries from {@code META-INF/microjainslee/sbb-index.properties}.
     * This supplements — does not replace — existing classpath discovery.
     *
     * <p>Idempotent: registering the same class twice silently replaces the
     * previous entry.
     *
     * @param sbbClass the SBB implementation class (must not be {@code null})
     */
    public static void registerSbb(Class<? extends com.microjainslee.api.Sbb> sbbClass) {
        if (sbbClass == null) {
            throw new IllegalArgumentException("sbbClass is required");
        }
        String className = sbbClass.getName();
        String name = sbbClass.getSimpleName();
        DeployableUnitIndexEntry entry = new DeployableUnitIndexEntry(
                className, name, "com.microjainslee", "1.0",
                Collections.singletonList(className),
                Collections.<String>emptyList(),
                Collections.<String>emptyList());
        programmaticDus.put(className, entry);
    }

    // ───────────────────────────────────────────────────────────────
    // P0 — event index build / freeze / lookup
    // ───────────────────────────────────────────────────────────────

    /**
     * Populate the mutable {@link #eventIndex} from the given {@link SbbIndex}.
     * Each event type's class name maps to the list of SBB class names that
     * handle it (derived from the deployable-unit sbbs list).
     * <p>Call this during deployment, then call {@link #freeze()} to seal
     * the index for zero-lock runtime reads.
     */
    public static void build(SbbIndex index) {
        if (index == null) {
            return;
        }
        for (SbbIndexEntry sbb : index.getSbbs()) {
            String sbbClass = sbb.getClassName();
            // Associate each event type with this SBB via the deployable units.
            for (DeployableUnitIndexEntry du : index.getDeployableUnits()) {
                if (du.getSbbs().contains(sbbClass)) {
                    // For each event type in the DU, map to this SBB.
                    for (EventTypeIndexEntry et : index.getEventTypes()) {
                        String eventClass = et.getClassName();
                        eventIndex.computeIfAbsent(eventClass,
                                k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(sbbClass);
                    }
                }
            }
        }
        // If no deployable units but we have SBBs and event types, map all to all.
        if (index.getDeployableUnits().isEmpty() && !index.getSbbs().isEmpty()
                && !index.getEventTypes().isEmpty()) {
            for (EventTypeIndexEntry et : index.getEventTypes()) {
                String eventClass = et.getClassName();
                List<String> sbbList = eventIndex.computeIfAbsent(eventClass,
                        k -> Collections.synchronizedList(new ArrayList<>()));
                for (SbbIndexEntry sbb : index.getSbbs()) {
                    sbbList.add(sbb.getClassName());
                }
            }
        }
    }

    /**
     * Freeze the mutable {@link #eventIndex} into an immutable
     * {@link #frozenIndex} for zero-lock reads. After freezing, all
     * lookups through {@link #lookupSbbs(String)} use the immutable
     * copy exclusively.
     */
    public static void freeze() {
        frozenIndex = Map.copyOf(eventIndex);
        eventIndex.clear();
    }

    /**
     * Look up the list of SBB class names registered to handle the given
     * event class. Prefers the frozen immutable copy when available;
     * falls back to the mutable build-time index for keys not found in
     * the frozen snapshot (supporting incremental builds after freeze).
     *
     * @param eventClassName the fully-qualified event class name
     * @return unmodifiable list of SBB class names, or empty list if none found
     */
    public static List<String> lookupSbbs(String eventClassName) {
        Map<String, List<String>> frozen = frozenIndex;
        if (frozen != null) {
            List<String> result = frozen.get(eventClassName);
            if (result != null) {
                return result;
            }
            // Fall through to eventIndex — entries built after freeze().
        }
        List<String> result = eventIndex.get(eventClassName);
        return result != null ? Collections.unmodifiableList(result) : Collections.emptyList();
    }

    /**
     * Returns {@code true} if {@link #freeze()} has been called and the
     * immutable index is available. Exposed for tests and diagnostics.
     */
    public static boolean isFrozen() {
        return frozenIndex != null;
    }

    /**
     * GOAL 3 — prepend programmatic deployable-unit entries to the
     * descriptor-based index. Programmatic entries come first so the
     * container processes them before any descriptor-based DU with the
     * same class name (the container's {@code if (resourceAdaptors.containsKey(...))}
     * guard makes the first-writer win).
     */
    private static SbbIndex mergeProgrammatic(SbbIndex descriptorIndex) {
        if (programmaticDus.isEmpty()) {
            return descriptorIndex;
        }
        List<DeployableUnitIndexEntry> mergedDus = new ArrayList<>(programmaticDus.values());
        mergedDus.addAll(descriptorIndex.getDeployableUnits());
        return new SbbIndex(
                descriptorIndex.getSbbs(),
                descriptorIndex.getEventTypes(),
                Collections.unmodifiableList(mergedDus));
    }

    static SbbIndex parse(Properties props) {
        List<SbbIndexEntry> sbbs = parseSection(props, "sbb", new EntryFactory<SbbIndexEntry>() {
            @Override
            public SbbIndexEntry create(int index, Properties props) {
                String clazz = required(props, "sbb." + index + ".class");
                return new SbbIndexEntry(clazz,
                        props.getProperty("sbb." + index + ".name", simpleName(clazz)),
                        props.getProperty("sbb." + index + ".vendor", "com.microjainslee"),
                        props.getProperty("sbb." + index + ".version", "1.0"));
            }
        });
        List<EventTypeIndexEntry> eventTypes = parseSection(props, "eventType", new EntryFactory<EventTypeIndexEntry>() {
            @Override
            public EventTypeIndexEntry create(int index, Properties props) {
                String clazz = required(props, "eventType." + index + ".class");
                return new EventTypeIndexEntry(clazz,
                        props.getProperty("eventType." + index + ".name", simpleName(clazz)),
                        props.getProperty("eventType." + index + ".vendor", "com.microjainslee"),
                        props.getProperty("eventType." + index + ".version", "1.0"));
            }
        });
        List<DeployableUnitIndexEntry> deployableUnits = parseSection(props, "du", new EntryFactory<DeployableUnitIndexEntry>() {
            @Override
            public DeployableUnitIndexEntry create(int index, Properties props) {
                String clazz = required(props, "du." + index + ".class");
                return new DeployableUnitIndexEntry(clazz,
                        props.getProperty("du." + index + ".name", simpleName(clazz)),
                        props.getProperty("du." + index + ".vendor", "com.microjainslee"),
                        props.getProperty("du." + index + ".version", "1.0"),
                        csv(props.getProperty("du." + index + ".sbbs")),
                        csv(props.getProperty("du." + index + ".ras")),
                        csv(props.getProperty("du." + index + ".profileSpecs")));
            }
        });
        return new SbbIndex(sbbs, eventTypes, deployableUnits);
    }

    private interface EntryFactory<T> {
        T create(int index, Properties props);
    }

    private static <T> List<T> parseSection(Properties props, String prefix, EntryFactory<T> factory) {
        int max = -1;
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(prefix + ".")) {
                int dot = key.indexOf('.', prefix.length() + 1);
                if (dot > 0) {
                    try {
                        int index = Integer.parseInt(key.substring(prefix.length() + 1, dot));
                        if (index > max) {
                            max = index;
                        }
                    } catch (NumberFormatException ignored) {
                        // skip malformed keys
                    }
                }
            }
        }
        if (max < 0) {
            return Collections.emptyList();
        }
        List<T> entries = new ArrayList<T>(max + 1);
        for (int i = 0; i <= max; i++) {
            if (props.containsKey(prefix + "." + i + ".class")) {
                entries.add(factory.create(i, props));
            }
        }
        return Collections.unmodifiableList(entries);
    }

    private static String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required index property: " + key);
        }
        return value.trim();
    }

    private static String simpleName(String fqn) {
        int dot = fqn.lastIndexOf('.');
        return dot >= 0 ? fqn.substring(dot + 1) : fqn;
    }

    private static List<String> csv(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = value.split(",");
        List<String> result = new ArrayList<String>(parts.length);
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static final class SbbIndex {
        private final List<SbbIndexEntry> sbbs;
        private final List<EventTypeIndexEntry> eventTypes;
        private final List<DeployableUnitIndexEntry> deployableUnits;

        public SbbIndex(List<SbbIndexEntry> sbbs,
                        List<EventTypeIndexEntry> eventTypes,
                        List<DeployableUnitIndexEntry> deployableUnits) {
            this.sbbs = sbbs;
            this.eventTypes = eventTypes;
            this.deployableUnits = deployableUnits;
        }

        public static SbbIndex empty() {
            return new SbbIndex(Collections.<SbbIndexEntry>emptyList(),
                    Collections.<EventTypeIndexEntry>emptyList(),
                    Collections.<DeployableUnitIndexEntry>emptyList());
        }

        public List<SbbIndexEntry> getSbbs() {
            return sbbs;
        }

        public List<EventTypeIndexEntry> getEventTypes() {
            return eventTypes;
        }

        public List<DeployableUnitIndexEntry> getDeployableUnits() {
            return deployableUnits;
        }

        public boolean isEmpty() {
            return sbbs.isEmpty() && eventTypes.isEmpty() && deployableUnits.isEmpty();
        }
    }

    public static final class SbbIndexEntry {
        private final String className;
        private final String name;
        private final String vendor;
        private final String version;

        public SbbIndexEntry(String className, String name, String vendor, String version) {
            this.className = className;
            this.name = name;
            this.vendor = vendor;
            this.version = version;
        }

        public String getClassName() {
            return className;
        }

        public String getName() {
            return name;
        }

        public String getVendor() {
            return vendor;
        }

        public String getVersion() {
            return version;
        }
    }

    public static final class EventTypeIndexEntry {
        private final String className;
        private final String name;
        private final String vendor;
        private final String version;

        public EventTypeIndexEntry(String className, String name, String vendor, String version) {
            this.className = className;
            this.name = name;
            this.vendor = vendor;
            this.version = version;
        }

        public String getClassName() {
            return className;
        }

        public String getName() {
            return name;
        }

        public String getVendor() {
            return vendor;
        }

        public String getVersion() {
            return version;
        }
    }

    public static final class DeployableUnitIndexEntry {
        private final String className;
        private final String name;
        private final String vendor;
        private final String version;
        private final List<String> sbbs;
        private final List<String> ras;
        private final List<String> profileSpecs;

        public DeployableUnitIndexEntry(String className, String name, String vendor, String version,
                                        List<String> sbbs, List<String> ras, List<String> profileSpecs) {
            this.className = className;
            this.name = name;
            this.vendor = vendor;
            this.version = version;
            this.sbbs = sbbs;
            this.ras = ras;
            this.profileSpecs = profileSpecs;
        }

        public String getClassName() {
            return className;
        }

        public String getName() {
            return name;
        }

        public String getVendor() {
            return vendor;
        }

        public String getVersion() {
            return version;
        }

        public List<String> getSbbs() {
            return sbbs;
        }

        public List<String> getRas() {
            return ras;
        }

        public List<String> getProfileSpecs() {
            return profileSpecs;
        }
    }
}
