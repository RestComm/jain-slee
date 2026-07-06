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

import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.api.OffHeapBindable;
import com.microjainslee.api.annotations.OffHeap;
import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.core.offheap.OffHeapArena;
import com.microjainslee.core.offheap.OffHeapRuntime;
import com.microjainslee.core.removal.EntityRemovalEvent;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * JAIN-SLEE 1.1 §8.2 — Per-SBB Virtual Thread Entity Pool with slot recycling.
 *
 * <p>Each active SBB entity id maps to a {@link EntitySlot} borrowed from an
 * internal {@link EntitySlotPool}. When the entity is released the slot (and
 * its parked virtual thread) is returned for reuse by a future session.
 */
public final class VirtualThreadSbbEntityPool {

    /** Per-SBB entity facade over a reusable {@link EntitySlot}. */
    public static final class SbbEntity {
        private final EntitySlot slot;
        private final String sbbId;
        private final Sbb sbb;

        SbbEntity(EntitySlot slot, String sbbId, Sbb sbb) {
            this.slot = slot;
            this.sbbId = sbbId;
            this.sbb = sbb;
        }

        EntitySlot getSlot() {
            return slot;
        }

        public String getSbbId() {
            return sbbId;
        }

        public Sbb getSbb() {
            return sbb;
        }

        public void submit(Runnable task) {
            slot.submit(task);
        }

        boolean isParked() {
            return slot.isParked();
        }

        boolean isShutdown() {
            return slot.isShutdown();
        }
    }

    private final ConcurrentHashMap<String, SbbEntity> entities =
            new ConcurrentHashMap<String, SbbEntity>();
    /**
     * Production P1.4 (S2) — cache of generated concrete SBB classes
     * keyed by the abstract SBB class. Populated lazily on first
     * {@link #acquireByClass(String, Class)} call when the codegen module
     * is reachable; stays empty in the reflection fallback mode.
     */
    private final ConcurrentHashMap<Class<?>, Class<?>> concreteClassCache =
            new ConcurrentHashMap<Class<?>, Class<?>>();
    private final EntitySlotPool slotPool;
    private final int min;
    private final int max;
    private final ExecutorService executor;
    /**
     * Reflection-resolved helper instance (lazily bound). {@code null} when
     * the codegen module is absent or {@link #codegenDisabled()} returns
     * {@code true}.
     */
    private volatile Object codegenHelper;
    /**
     * Class name used for the reflective lookup of the codegen helper.
     * Held in a {@code static final} field so it can be swapped out by
     * tests via {@link #setCodegenHelperClassName(String)} without
     * touching the production wiring.
     */
    private static volatile String codegenHelperClassName =
            "com.microjainslee.codegen.JavassistDeployTimeCodegen";
    private final Path deployDir;
    private volatile boolean shuttingDown;
    /** GOAL 4 — container reference for @InjectRa resolution. Set by MicroSleeContainer. */
    private volatile MicroSleeContainer container;

    public VirtualThreadSbbEntityPool(int min, int max, boolean perVirtualThread) {
        this(min, max, perVirtualThread, defaultDeployDir());
    }

    /**
     * Production P1.4 (S2) constructor — accepts an explicit deploy
     * directory so the pool's codegen helper writes {@code .class} files
     * to the configured location instead of the JVM default temp dir.
     *
     * @param min               minimum number of warm slots
     * @param max               maximum number of slots
     * @param perVirtualThread  when {@code true} run tasks on virtual threads
     * @param deployDir         directory for generated .class files
     */
    public VirtualThreadSbbEntityPool(int min, int max, boolean perVirtualThread, Path deployDir) {
        if (min < 0) {
            throw new IllegalArgumentException("min must be >= 0");
        }
        if (max < 1) {
            throw new IllegalArgumentException("max must be >= 1");
        }
        if (min > max) {
            throw new IllegalArgumentException("min (" + min + ") must be <= max (" + max + ")");
        }
        this.min = min;
        this.max = max;
        ExecutorService chosen = null;
        if (perVirtualThread) {
            chosen = MicroSleeExecutors.newVirtualThreadPerTaskExecutor();
        }
        if (chosen == null) {
            chosen = java.util.concurrent.Executors.newCachedThreadPool();
        }
        this.executor = chosen;
        this.slotPool = new EntitySlotPool(min, max, executor);
        this.deployDir = deployDir == null ? defaultDeployDir() : deployDir;
    }

    /**
     * Default deploy directory — respects the {@code jainslee.deploy.dir}
     * system property, falling back to {@code ${java.io.tmpdir}/slee-deploy}.
     */
    private static Path defaultDeployDir() {
        String sysProp = System.getProperty("jainslee.deploy.dir");
        if (sysProp != null && !sysProp.isBlank()) {
            return Paths.get(sysProp);
        }
        return Paths.get(System.getProperty("java.io.tmpdir"), "slee-deploy");
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 4 — RaCommandPort injection support
    // ──────────────────────────────────────────────────────────

    /** Called by {@link MicroSleeContainer} after construction. */
    void setContainer(MicroSleeContainer container) {
        this.container = container;
    }

    /**
     * Scan the SBB instance for {@code @InjectRa} fields and inject
     * the matching {@link RaCommandPort} from the container.
     */
    private void injectRaPorts(Sbb sbb) {
        MicroSleeContainer c = this.container;
        if (c == null || sbb == null) {
            return;
        }
        for (Field field : sbb.getClass().getDeclaredFields()) {
            InjectRa injectRa = field.getAnnotation(InjectRa.class);
            if (injectRa == null) {
                continue;
            }
            String raName = injectRa.name();
            RaCommandPort port = (raName == null || raName.isEmpty())
                    ? c.getDefaultRaCommandPort()
                    : c.getRaCommandPort(raName);
            if (port != null) {
                field.setAccessible(true);
                try {
                    field.set(sbb, port);
                } catch (IllegalAccessException e) {
                    // Should not happen after setAccessible(true)
                }
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // Off-heap CMP state (docs/en/design-offheap-sbb-state.md §6)
    // ──────────────────────────────────────────────────────────

    /** One arena per @OffHeap-annotated class (the annotation owner). */
    private final ConcurrentHashMap<Class<?>, OffHeapArena> offHeapArenas =
            new ConcurrentHashMap<>();

    /** Live arenas — exposed for the autonomous guardian (compaction). */
    public java.util.Collection<OffHeapArena> getOffHeapArenas() {
        return java.util.Collections.unmodifiableCollection(offHeapArenas.values());
    }

    private void bindOffHeapIfNeeded(Sbb sbb, String sbbId) {
        if (!(sbb instanceof OffHeapBindable bindable)) {
            return;
        }
        Class<?> owner = offHeapOwner(sbb.getClass());
        if (owner == null) {
            return;
        }
        MicroSleeContainer c = this.container;
        if (c != null && !c.getConfiguration().isOffHeapEnabled()) {
            return; // fallback-mode SBBs keep working on the heap path
        }
        OffHeapArena arena = offHeapArenas.computeIfAbsent(owner, this::createArena);
        long base = arena.allocate(sbbId);
        bindable.setEntityId(sbbId);
        bindable.bindSlot(base);
    }

    private void injectOffHeapForPrebuilt(Sbb sbb, String sbbId) {
        bindOffHeapIfNeeded(sbb, sbbId);
    }

    private void unbindOffHeapIfNeeded(Sbb sbb, String sbbId) {
        if (!(sbb instanceof OffHeapBindable bindable)) {
            return;
        }
        Class<?> owner = offHeapOwner(sbb.getClass());
        if (owner == null) {
            return;
        }
        bindable.unbindSlot();
        OffHeapArena arena = offHeapArenas.get(owner);
        if (arena != null) {
            arena.free(sbbId);
        }
    }

    private static Class<?> offHeapOwner(Class<?> cls) {
        for (Class<?> c = cls; c != null && c != Object.class; c = c.getSuperclass()) {
            if (c.getAnnotation(OffHeap.class) != null) {
                return c;
            }
        }
        return null;
    }

    private OffHeapArena createArena(Class<?> owner) {
        OffHeap ann = owner.getAnnotation(OffHeap.class);
        var layout = OffHeapRuntime.layoutFor(owner);
        Path file = null;
        if (ann.storage() == com.microjainslee.api.annotations.StorageType.MMAP) {
            String path = ann.filePath();
            if (path == null || path.isBlank()) {
                MicroSleeContainer c = this.container;
                String dir = c != null ? c.getConfiguration().getOffHeapStorageDir() : "";
                if (dir == null || dir.isBlank()) {
                    dir = System.getProperty("java.io.tmpdir") + "/slee-offheap";
                }
                path = dir + "/" + owner.getSimpleName() + ".slab";
            }
            file = Paths.get(path);
        }
        OffHeapArena arena = new OffHeapArena(owner.getSimpleName(), layout,
                ann.maxSlots(), file);
        // Compaction rebinding: when the guardian compacts the arena, live
        // entities must see their new base address immediately.
        arena.setSlotMovedListener((entityId, newAddr) -> {
            SbbEntity entity = entities.get(entityId);
            if (entity != null && entity.getSbb() instanceof OffHeapBindable ob) {
                ob.bindSlot(newAddr);
            }
        });
        return arena;
    }

    public SbbEntity acquire(String sbbId, Supplier<Sbb> factory) {
        if (shuttingDown) {
            throw new IllegalStateException("SbbEntityPool is shutting down");
        }
        if (sbbId == null || factory == null) {
            throw new IllegalArgumentException("sbbId and factory are required");
        }
        SbbEntity existing = entities.get(sbbId);
        if (existing != null) {
            return existing;
        }
        Sbb sbb = factory.get();
        injectRaPorts(sbb);
        bindOffHeapIfNeeded(sbb, sbbId);
        EntitySlot slot = slotPool.borrow();
        slot.bind(sbbId, 0L, sbb);
        SbbEntity fresh = new SbbEntity(slot, sbbId, sbb);
        SbbEntity prior = entities.putIfAbsent(sbbId, fresh);
        if (prior != null) {
            slot.unbind();
            slotPool.release(slot);
            return prior;
        }
        return fresh;
    }

    public SbbEntity acquire(String sbbId, long entityId, Sbb sbb) {
        if (shuttingDown) {
            throw new IllegalStateException("SbbEntityPool is shutting down");
        }
        if (sbbId == null || sbb == null) {
            throw new IllegalArgumentException("sbbId and sbb are required");
        }
        SbbEntity existing = entities.get(sbbId);
        if (existing != null) {
            return existing;
        }
        injectRaPorts(sbb);
injectOffHeapForPrebuilt(sbb, sbbId);
                EntitySlot slot = slotPool.borrow();
        slot.bind(sbbId, entityId, sbb);
        SbbEntity fresh = new SbbEntity(slot, sbbId, sbb);
        SbbEntity prior = entities.putIfAbsent(sbbId, fresh);
        if (prior != null) {
            slot.unbind();
            slotPool.release(slot);
            return prior;
        }
        return fresh;
    }

    /**
     * Release the entity for {@code sbbId} and return its slot to the idle pool.
     */
    public void release(SbbEntity entity) {
        if (entity == null) {
            return;
        }
        entities.remove(entity.getSbbId(), entity);
        unbindOffHeapIfNeeded(entity.getSbb(), entity.getSbbId());
        EntitySlot slot = entity.getSlot();
        slot.unbind();
        slotPool.release(slot);
    }

    public void releaseById(String sbbId) {
        SbbEntity entity = entities.remove(sbbId);
        if (entity != null) {
            unbindOffHeapIfNeeded(entity.getSbb(), sbbId);
            EntitySlot slot = entity.getSlot();
            slot.unbind();
            slotPool.release(slot);
        }
    }

    public SbbEntity findEntity(String sbbId) {
        if (sbbId == null) {
            return null;
        }
        return entities.get(sbbId);
    }

    public int prewarm(int count) {
        return slotPool.prewarm(count);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int size() {
        return entities.size();
    }

    public int idleSlotCount() {
        return slotPool.idleCount();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public boolean isShutdown() {
        return shuttingDown || executor.isShutdown();
    }

    public void shutdown() {
        shuttingDown = true;
        offHeapArenas.values().forEach(OffHeapArena::close);
        offHeapArenas.clear();
        for (SbbEntity entity : entities.values()) {
            entity.getSlot().markShutdown();
        }
        slotPool.shutdown();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        entities.clear();
    }

    // ----------------------------------------------------------------
    //  Production P1.4 (S2) — Javassist CMP codegen wiring
    // ----------------------------------------------------------------

    /**
     * Acquire an entity backed by a concrete SBB class generated from the
     * supplied abstract {@code sbbClass} via the Javassist codegen helper.
     *
     * <p>The concrete class is generated on first call and cached in
     * {@link #concreteClassCache}. Subsequent calls reuse the cached
     * class, so the hot path stays at {@code O(1)}.
     */
    public SbbEntity acquireByClass(String sbbId, Class<? extends Sbb> sbbClass) {
        if (shuttingDown) {
            throw new IllegalStateException("SbbEntityPool is shutting down");
        }
        if (sbbId == null || sbbClass == null) {
            throw new IllegalArgumentException("sbbId and sbbClass are required");
        }
        SbbEntity existing = entities.get(sbbId);
        if (existing != null) {
            return existing;
        }
        Sbb sbb = instantiateSbb(sbbClass);
        injectRaPorts(sbb);
        EntitySlot slot = slotPool.borrow();
        slot.bind(sbbId, 0L, sbb);
        SbbEntity fresh = new SbbEntity(slot, sbbId, sbb);
        SbbEntity prior = entities.putIfAbsent(sbbId, fresh);
        if (prior != null) {
            slot.unbind();
            slotPool.release(slot);
            return prior;
        }
        return fresh;
    }

    /**
     * Instantiate the SBB class, preferring the codegen-generated concrete
     * class when the {@code jainslee-codegen} module is reachable.
     */
    private Sbb instantiateSbb(Class<? extends Sbb> sbbClass) {
        Class<?> concrete = concreteClassCache.get(sbbClass);
        if (concrete == null) {
            concrete = resolveConcreteClass(sbbClass);
            if (concrete != null) {
                concreteClassCache.put(sbbClass, concrete);
            }
        }
        try {
            if (concrete != null) {
                return (Sbb) concrete.getDeclaredConstructor().newInstance();
            }
            return sbbClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate SBB: " + sbbClass.getName()
                    + (concrete != null ? " (concrete=" + concrete.getName() + ")" : ""), e);
        }
    }

    /**
     * Resolve the concrete SBB class for {@code sbbClass}. Returns
     * {@code null} when the codegen module is not on the classpath or
     * has been disabled via {@link #setCodegenDisabled(boolean)}.
     */
    private Class<?> resolveConcreteClass(Class<? extends Sbb> sbbClass) {
        if (codegenDisabled) {
            return null;
        }
        Object helper = codegenHelper;
        if (helper == null) {
            helper = tryLoadCodegenHelper();
            codegenHelper = helper;
        }
        if (helper == null) {
            // Module absent; cache the negative result so we don't keep probing.
            return null;
        }
        try {
            java.lang.reflect.Method getOrGenerate = helper.getClass()
                    .getMethod("getOrGenerate", Class.class, java.nio.file.Path.class);
            @SuppressWarnings("unchecked")
            Class<? extends Sbb> generated =
                    (Class<? extends Sbb>) getOrGenerate.invoke(helper, sbbClass, deployDir);
            return generated;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Codegen helper failed for " + sbbClass.getName(), e);
        }
    }

    /**
     * Reflectively probe for the codegen helper class. Returns {@code null}
     * when the helper is not on the runtime classpath.
     */
    private Object tryLoadCodegenHelper() {
        final String className = codegenHelperClassName;
        if (className == null) {
            return null;
        }
        try {
            Class<?> helperClass = Class.forName(className, true,
                    Thread.currentThread().getContextClassLoader());
            return helperClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to instantiate codegen helper " + className, e);
        }
    }

    private static volatile boolean codegenDisabled = false;

    /**
     * Hook for embedders and tests: force the codegen helper off even if
     * the module is on the runtime classpath. Default {@code false}.
     */
    public static void setCodegenDisabled(boolean disabled) {
        codegenDisabled = disabled;
    }

    /**
     * Test hook: replace the codegen helper class name. Production code
     * should leave this alone.
     */
    public static void setCodegenHelperClassName(String name) {
        codegenHelperClassName = name;
    }

    /**
     * Test hook: drop the concrete-class cache so the next acquire
     * regenerates. Visible for {@code MicroSleeContainer} integration
     * tests that redeploy.
     */
    public void invalidateConcreteClassCache() {
        concreteClassCache.clear();
        codegenHelper = null;
    }

    /**
     * Return the directory where generated .class files are persisted.
     */
    public Path getDeployDir() {
        return deployDir;
    }

    // ----------------------------------------------------------------
    //  S4 — ChildRelation wiring (com.microjainslee.core.child)
    // ----------------------------------------------------------------

    /**
     * Create (or fetch the cached) child relation bound to {@code parentEntityId}
     * for child SBB local-interface class {@code childClass}. Used by the
     * container-side wiring so each parent entity owns one
     * {@link com.microjainslee.core.child.ChildRelationImpl} per child type.
     *
     * <p>The supplied {@code factory} must be non-null; if it is {@code null}
     * this method returns {@code null} (callers can check + fall back).
     *
     * @param parentEntityId id of the parent SBB entity
     * @param childClass     SBB local-interface class this relation spawns
     * @param factory       {@link com.microjainslee.core.child.ChildRelationFactory}
     *                       that owns the {@link com.microjainslee.core.child.CascadeRemover}
     * @return the cached relation, or {@code null} when {@code factory} is null
     */
    public <T extends com.microjainslee.api.SbbLocalObject>
            com.microjainslee.core.child.ChildRelationImpl<T> createChildRelation(
                    String parentEntityId,
                    Class<T> childClass,
                    com.microjainslee.core.child.ChildRelationFactory factory) {
        if (factory == null) {
            return null;
        }
        return factory.createChildRelation(parentEntityId, childClass);
    }

    /**
     * Build a {@link com.microjainslee.core.child.CascadeRemover.EntityLookup}
     * bound to this pool. Returned lookup delegates {@link
     * #findEntity(String)} + {@link #releaseById(String)} so cascade
     * removal drives the entity lifecycle through the pool.
     */
    public com.microjainslee.core.child.CascadeRemover.EntityLookup asEntityLookup() {
        final VirtualThreadSbbEntityPool self = this;
        return new com.microjainslee.core.child.CascadeRemover.EntityLookup() {
            @Override
            public Sbb getSbb(String entityId) {
                SbbEntity e = entities.get(entityId);
                return e != null ? e.getSbb() : null;
            }

            @Override
            public void releaseEntity(String entityId) {
                self.releaseById(entityId);
            }
        };
    }

    private static final class NoopSbb implements Sbb { }

    // ───────────────────────────────────────────────────────────────
    // Sprint S6 — IES cleanup adapter (closes GAP-SR-7)
    // ───────────────────────────────────────────────────────────────

    /**
     * SPI adapter: bridges {@link com.microjainslee.core.removal.EntityRemovalBus}
     * events into the IES dispatcher's convergence-cleanup callback. Registered
     * once during container startup via
     * {@code MicroSleeContainer.setInitialEventSelectorDispatcher(...)}.
     *
     * <p><b>Why an inner class?</b> Keeps the SPI concern isolated; the pool
     * itself remains agnostic of IES internals and the kernel can wire the
     * two together through the bus without the pool knowing about IES at all.
     *
     * <p><b>GAP-SR-7</b>: previously the {@code InitialEventSelectorDispatcher}
     * never received a removal notification when an SBB entity was recycled,
     * leaving stale convergence mappings in the dispatcher index. This
     * adapter wires {@code removeConvergencesFor(entityId)} so every entity
     * removal cleans up its convergence(s) automatically.
     */
    public static final class IesCleanupAdapter
            implements Consumer<EntityRemovalEvent> {

        private final InitialEventSelectorDispatcher iesDispatcher;

        public IesCleanupAdapter(InitialEventSelectorDispatcher iesDispatcher) {
            if (iesDispatcher == null) {
                throw new IllegalArgumentException("iesDispatcher is required");
            }
            this.iesDispatcher = iesDispatcher;
        }

        @Override
        public void accept(EntityRemovalEvent event) {
            if (event == null) {
                return;
            }
            String entityId = event.entityId();
            if (entityId != null) {
                iesDispatcher.removeConvergencesFor(entityId);
            }
        }
    }
}
