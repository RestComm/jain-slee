/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.codegen;

import com.microjainslee.api.OffHeapBindable;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.annotations.CmpField;
import com.microjainslee.api.annotations.OffHeap;
import com.microjainslee.api.annotations.StorageType;
import com.microjainslee.core.CmpFieldStoreLocator;
import com.microjainslee.core.InMemoryCmpFieldStore;
import com.microjainslee.core.offheap.OffHeapArena;
import com.microjainslee.core.offheap.OffHeapRuntime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Method;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the {@code @OffHeap} codegen path (design doc §5):
 * generated {@code $Concrete} implements {@link OffHeapBindable}, its
 * accessors read/write the bound slot with no CmpFieldStore involvement,
 * and {@code fallback = true} routes to the heap path while unbound.
 */
class OffHeapCodegenTest {

    @OffHeap(storage = StorageType.DIRECT, maxSlots = 64)
    public abstract static class OffHeapSessionSbb implements Sbb {
        @CmpField("msisdn")
        public abstract String getMsisdn();
        @CmpField("msisdn")
        public abstract void setMsisdn(String v);

        @CmpField("menuState")
        public abstract int getMenuState();
        @CmpField("menuState")
        public abstract void setMenuState(int v);

        @CmpField("startedAt")
        public abstract long getStartedAt();
        @CmpField("startedAt")
        public abstract void setStartedAt(long v);
    }

    @OffHeap(storage = StorageType.DIRECT, maxSlots = 64, fallback = true)
    public abstract static class DualModeSbb implements Sbb {
        @CmpField("note")
        public abstract String getNote();
        @CmpField("note")
        public abstract void setNote(String v);
    }

    @TempDir
    Path deployDir;

    private ConcreteSbbGenerator generator;

    @BeforeEach
    void setup() {
        generator = new ConcreteSbbGenerator();
        CmpFieldStoreLocator.set(new InMemoryCmpFieldStore());
    }

    @AfterEach
    void teardown() {
        CmpFieldStoreLocator.set(null);
    }

    private static Object call(Object target, String method, Object... args) throws Exception {
        for (Method m : target.getClass().getMethods()) {
            if (m.getName().equals(method) && m.getParameterCount() == args.length) {
                return m.invoke(target, args);
            }
        }
        throw new NoSuchMethodException(method);
    }

    @Test
    @DisplayName("generated concrete implements OffHeapBindable and hits the slot")
    void offHeapAccessorsReadWriteTheBoundSlot() throws Exception {
        Class<?> concrete = generator.getOrGenerate(OffHeapSessionSbb.class, deployDir);
        Object sbb = concrete.getDeclaredConstructor().newInstance();
        assertThat(sbb).isInstanceOf(OffHeapBindable.class);
        OffHeapBindable bindable = (OffHeapBindable) sbb;

        try (OffHeapArena arena = new OffHeapArena("codegen-test",
                OffHeapRuntime.layoutFor(OffHeapSessionSbb.class), 64)) {
            long base = arena.allocate("e1");
            bindable.setEntityId("e1");
            bindable.bindSlot(base);
            assertThat(bindable.slotAddress()).isEqualTo(base);

            call(sbb, "setMsisdn", "84911222333");
            call(sbb, "setMenuState", 5);
            call(sbb, "setStartedAt", 999_999L);

            assertThat(call(sbb, "getMsisdn")).isEqualTo("84911222333");
            assertThat(call(sbb, "getMenuState")).isEqualTo(5);
            assertThat(call(sbb, "getStartedAt")).isEqualTo(999_999L);

            // A second instance bound to the same slot sees the same state —
            // proof the state lives off-heap, not in object fields.
            Object twin = concrete.getDeclaredConstructor().newInstance();
            ((OffHeapBindable) twin).bindSlot(base);
            assertThat(call(twin, "getMsisdn")).isEqualTo("84911222333");
            assertThat(call(twin, "getMenuState")).isEqualTo(5);

            bindable.unbindSlot();
            assertThat(bindable.slotAddress()).isZero();
            // Unbound (no fallback): defaults, never a crash.
            assertThat(call(sbb, "getMsisdn")).isNull();
            assertThat(call(sbb, "getMenuState")).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("fallback=true uses the heap path while unbound, slot once bound")
    void dualModeFallsBackToHeapWhenUnbound() throws Exception {
        Class<?> concrete = generator.getOrGenerate(DualModeSbb.class, deployDir);
        Object sbb = concrete.getDeclaredConstructor().newInstance();

        // Unbound → heap path (in-memory fallback map inside $Concrete).
        call(sbb, "setNote", "heap-mode");
        assertThat(call(sbb, "getNote")).isEqualTo("heap-mode");

        try (OffHeapArena arena = new OffHeapArena("dual-test",
                OffHeapRuntime.layoutFor(DualModeSbb.class), 64)) {
            OffHeapBindable bindable = (OffHeapBindable) sbb;
            bindable.setEntityId("d1");
            bindable.bindSlot(arena.allocate("d1"));

            call(sbb, "setNote", "offheap-mode");
            assertThat(call(sbb, "getNote")).isEqualTo("offheap-mode");

            bindable.unbindSlot();
            // Back on the heap path — the old heap value is still there.
            assertThat(call(sbb, "getNote")).isEqualTo("heap-mode");
        }
    }
}
