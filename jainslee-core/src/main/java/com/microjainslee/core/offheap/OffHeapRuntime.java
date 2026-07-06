/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import com.microjainslee.api.annotations.CmpField;
import com.microjainslee.api.annotations.OffHeap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class-level off-heap metadata registry. Reflection happens exactly once
 * per SBB class (registration time); the hot path only ever touches the
 * cached {@link OffHeapLayout}. Generated {@code $Concrete} accessors call
 * {@link #layoutFor(Class)} from a lazily-initialised static field.
 */
public final class OffHeapRuntime {

    private static final ConcurrentHashMap<Class<?>, OffHeapLayout> LAYOUTS =
            new ConcurrentHashMap<>();

    private OffHeapRuntime() {
    }

    /** The {@code @OffHeap} annotation for the class (walks superclasses). */
    public static OffHeap annotationOf(Class<?> sbbClass) {
        for (Class<?> c = sbbClass; c != null && c != Object.class; c = c.getSuperclass()) {
            OffHeap ann = c.getAnnotation(OffHeap.class);
            if (ann != null) {
                return ann;
            }
        }
        return null;
    }

    public static boolean isOffHeap(Class<?> sbbClass) {
        return annotationOf(sbbClass) != null;
    }

    /**
     * Layout for an {@code @OffHeap} SBB class, derived from its
     * {@code @CmpField} getter signatures. Deterministic field order
     * (declaration order, superclasses first) so mmap recovery across
     * restarts sees identical offsets.
     */
    public static OffHeapLayout layoutFor(Class<?> sbbClass) {
        return LAYOUTS.computeIfAbsent(sbbClass, OffHeapRuntime::buildLayout);
    }

    private static OffHeapLayout buildLayout(Class<?> sbbClass) {
        OffHeap ann = annotationOf(sbbClass);
        if (ann == null) {
            throw new IllegalArgumentException(sbbClass.getName() + " is not @OffHeap");
        }
        Map<String, Class<?>> fields = new LinkedHashMap<>();
        collectCmpGetters(sbbClass, fields);
        if (fields.isEmpty()) {
            throw new IllegalArgumentException(sbbClass.getName()
                    + " is @OffHeap but declares no @CmpField getters");
        }
        List<OffHeapLayout.FieldSpec> specs = new ArrayList<>(fields.size());
        for (Map.Entry<String, Class<?>> e : fields.entrySet()) {
            specs.add(OffHeapLayout.FieldSpec.forJavaType(
                    e.getKey(), e.getValue(), ann.maxFieldBytes()));
        }
        return OffHeapLayout.of(specs, ann.slotSize());
    }

    private static void collectCmpGetters(Class<?> sbbClass, Map<String, Class<?>> out) {
        if (sbbClass == null || sbbClass == Object.class) {
            return;
        }
        collectCmpGetters(sbbClass.getSuperclass(), out); // superclasses first
        for (Method m : sbbClass.getDeclaredMethods()) {
            CmpField cmp = m.getAnnotation(CmpField.class);
            if (cmp == null || m.getParameterCount() != 0
                    || m.getReturnType() == void.class) {
                continue; // setters and non-getters are skipped
            }
            String name = cmp.value().isEmpty() ? deriveName(m.getName()) : cmp.value();
            out.putIfAbsent(name, m.getReturnType());
        }
    }

    private static String deriveName(String methodName) {
        String stripped = methodName.startsWith("get") ? methodName.substring(3)
                : methodName.startsWith("is") ? methodName.substring(2)
                : methodName;
        return stripped.isEmpty() ? methodName
                : Character.toLowerCase(stripped.charAt(0)) + stripped.substring(1);
    }
}
