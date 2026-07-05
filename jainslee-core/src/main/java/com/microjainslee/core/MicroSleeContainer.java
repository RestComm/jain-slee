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

import com.microjainslee.api.ActivityContextHandle;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ActivityContextNamingFacility;
import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.Address;
import com.microjainslee.api.CreateException;
import com.microjainslee.api.InitialEventSelector;
import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.PoolableSbb;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.ResourceAdaptor;
import com.microjainslee.ra.RaEntityStateMachine;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbID;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.ServiceID;
import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.TimerPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.core.removal.EntityRemovalBus;
import com.microjainslee.core.removal.EntityRemovalEvent;
import com.microjainslee.core.removal.EntityRemovalEvent.RemovalReason;
import com.microjainslee.core.removal.SessionLifecycleLogger;
import com.microjainslee.core.ordering.OutOfOrderBuffer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Embedded JAIN-SLEE micro-container foundation with no JBoss Modules, VFS, MSC, or JMX dependency.
 */
public final class MicroSleeContainer {

    private static final Logger LOG = LogManager.getLogger(MicroSleeContainer.class);

    public enum State {
        CREATED, STARTED, STOPPED
    }

    private final MicroSleeConfiguration configuration;
    // Production P2.2 — the activity context naming facility is
    // mutable so a clustered (Infinispan-backed) implementation
    // can be installed after construction through
    // #bindActivityContextNamingFacility(Object). The initial value
    // is the in-memory facility; reflective rebind swaps the
    // reference atomically (volatile) without affecting the rest
    // of the kernel.
    private volatile AcnfBackend activityContextNamingFacility;
    private final EventRouter eventRouter;
    private final TimerPortImpl timerPort;
    private volatile VirtualThreadSbbEntityPool sbbEntityPool;
    private final SbbObjectPool sbbObjectPool;
    private final ActivityContextPool aciPool;
    private final SbbTypeRegistry sbbTypeRegistry;
    private final EntityIdAllocator entityIdAllocator = new EntityIdAllocator();
    private final ConcurrentHashMap<String, Class<? extends Sbb>> entityTypesById =
            new ConcurrentHashMap<String, Class<? extends Sbb>>();
    private final ServiceRegistry serviceRegistry = new ServiceRegistry();
    private final InMemoryCmpFieldStore cmpFieldStore = new InMemoryCmpFieldStore();
    private final InMemoryProfileFacility profileFacility = new InMemoryProfileFacility();
    private final SimpleAlarmPort alarmPort = new SimpleAlarmPort();
    private final SimpleAlarmFacility alarmFacility = new SimpleAlarmFacility(alarmPort);
    private final SbbLifecycleManager sbbLifecycleManager = new SbbLifecycleManager();
    private final ConcurrentHashMap<String, SimpleSbbLocalObject> sbbs =
            new ConcurrentHashMap<String, SimpleSbbLocalObject>();
    /**
     * GOAL 2 — deterministic root-SBB fallback. {@link #sbbs} is a
     * ConcurrentHashMap whose iteration order is undefined, so the legacy
     * "first registered SBB" fallback used to pick an arbitrary entity.
     * This list preserves registration order; stale ids (already removed
     * from {@link #sbbs}) are skipped on read and pruned lazily.
     */
    private final java.util.concurrent.CopyOnWriteArrayList<String> sbbRegistrationOrder =
            new java.util.concurrent.CopyOnWriteArrayList<String>();
    /**
     * GOAL 3 — programmatic registration takes priority over
     * descriptor-based (classpath sbb-index) registration. Entities that
     * were auto-deployed from the index are tracked here so the root-SBB
     * fallback prefers explicitly registered SBBs.
     */
    private final Set<String> descriptorRegisteredIds = ConcurrentHashMap.newKeySet();
    /** True only while {@link #autoDeployFromClasspathIndex()} runs (inside start()). */
    private volatile boolean autoDeployInProgress;
    private final ConcurrentHashMap<String, RaBootstrapContextImpl> resourceAdaptors =
            new ConcurrentHashMap<String, RaBootstrapContextImpl>();
    /** GOAL 4 — registered RaCommandPort instances keyed by RA entity name. */
    private final ConcurrentHashMap<String, RaCommandPort> raCommandPorts =
            new ConcurrentHashMap<String, RaCommandPort>();
    private volatile State state = State.CREATED;
    private volatile ClassLoader deploymentClassLoader;
    private volatile InitialEventSelectorCustomizer initialEventSelectorCustomizer;

    // Sprint S6 — Observability & Removal Notification.
    private final EntityRemovalBus entityRemovalBus = new EntityRemovalBus();
    private final AtomicLong entityRemovalCounter = new AtomicLong();
    /**
     * Sprint S6 — IES dispatcher reference (typed via reflection at bind time
     * so {@code MicroSleeContainer} does not pull a compile-time edge to the
     * {@code ies} package). Used to (a) capture the convergence key right
     * before an entity is released, and (b) wire
     * {@link VirtualThreadSbbEntityPool.IesCleanupAdapter} on the
     * {@link EntityRemovalBus} when the dispatcher is bound.
     */
    private volatile Object iesDispatcher;

    /**
     * Sprint S8.5 — shared dedup window for {@link com.microjainslee.ra.SleeEndpointImpl}.
     *
     * <p>Created lazily on {@link #start()} (60-second window, 65 536-entry
     * LRU) and passed into {@link com.microjainslee.core.ra.ResourceAdaptorContextBuilder}
     * so every {@code SleeEndpointImpl} the container builds gets its
     * {@code DedupCheck} and {@code dedup hit counter} wired automatically.
     * Embedders that don't want a dedup window can call
     * {@link #setDedupWindow(com.microjainslee.core.ordering.DedupWindow)}
     * with {@code null} before {@code start()} returns, or call
     * {@link #disableDedupWindow()} right after construction.</p>
     */
    private volatile com.microjainslee.core.ordering.DedupWindow dedupWindow;

    /**
     * Sprint S8.5 — flag that lets embedders opt out of the default
     * 60-second window. Checked once inside {@link #start()}; afterwards
     * the {@link #dedupWindow} field stays non-null for the lifetime of
     * the container (or until explicitly cleared).
     */
    private volatile boolean dedupWindowDisabled;
    /**
     * Sprint S8 — per-convergence out-of-order buffer for non-initial
     * {@code SequencedEvent}s that arrive before the SBB entity is
     * allocated (S8.3). The buffer is created lazily in {@link #start()}
     * and shut down in {@link #stop()}; if the IES dispatcher is bound
     * after {@code start()}, the buffer is pushed into the dispatcher
     * reflectively during {@link #setInitialEventSelectorDispatcher(Object)}.
     *
     * <p>Defaults: capacity 16 per convergence, TTL 30 000 ms,
     * evictor sweep 30 000 ms.</p>
     */
    private volatile OutOfOrderBuffer outOfOrderBuffer;
    /**
     * Sprint S6 — structured lifecycle logger auto-registered during
     * {@link #start()} and removed during {@link #stop()} to avoid
     * leaking subscribers across stop/start cycles.
     */
    private volatile SessionLifecycleLogger sessionLifecycleLogger;
    // Production P2.1 — optional cluster manager. Bound reflectively by
    // #bindCluster(Object) when MicroSleeConfiguration.isClusterEnabled() is
    // true. Stored as java.lang.Object so the kernel stays free of any
    // jainslee-cluster compile-time dependency.
    private volatile Object clusterManager;

    public MicroSleeContainer() {
        this(MicroSleeConfiguration.defaults());
    }

    public MicroSleeContainer(MicroSleeConfiguration configuration) {
        this.configuration = configuration;
        this.activityContextNamingFacility = new InMemoryAcnfBackend(new InMemoryActivityContextNamingFacility());
        this.eventRouter = new EventRouter(configuration.getEventRouterBufferSize(),
                configuration.isPreferVirtualThreads(),
                configuration.isSbbPerVirtualThread(),
                configuration.getEventDeliveryMode());
        this.timerPort = TimerPortImpl.create(eventRouter);
        this.sbbEntityPool = newSbbEntityPool();
        this.sbbTypeRegistry = new SbbTypeRegistry(sbbLifecycleManager,
                configuration.getSbbPoolMax());
        this.eventRouter.bindSbbEntityPool(this.sbbEntityPool);
        this.eventRouter.bindTransactionSupport(timerPort.getBridge(),
                new DefaultErrorHandlingPolicy(timerPort.getBridge()));
        // Production P1.2 — wire the optional Narayana JTA transaction
        // context. Created reflectively so the jainslee-core compile classpath
        // does NOT depend on jainslee-tx. When txEnabled=false the kernel
        // behaves exactly as before — NoOpTransactionManager is a true
        // no-op and the EventRouter wrapper sees null.
        this.eventRouter.bindJtaTransactionContext(
                createJtaTransactionContext(configuration.isTxEnabled()));
        // §6 / §7 — SBB Object Pool and Activity Context Pool, both backed
        // by JCTools MPMC queues (audit G2 / G3). Capacities mirror the
        // existing SBB-entity-pool sizing so a single allocation covers
        // both layers in steady state.
        this.sbbObjectPool = new SbbObjectPool(64, Math.max(4096,
                configuration.getSbbPoolMax()), new java.util.function.Supplier<com.microjainslee.api.Sbb>() {
            @Override
            public com.microjainslee.api.Sbb get() {
                // Pool factory is only used when callers explicitly acquire
                // via getSbbObjectPool(); the entity-pool path in
                // registerSbb() constructs SBBs directly. We return a
                // no-op here so an accidental acquire() never produces
                // half-initialised state.
                return new com.microjainslee.api.Sbb() { };
            }
        });
        this.aciPool = new ActivityContextPool(32, 2048,
                new java.util.function.Supplier<InMemoryActivityContext>() {
            @Override
            public InMemoryActivityContext get() {
                // The ACI pool's factory supplies a placeholder name; callers
                // (createActivityContext) rename / rebind the context after
                // acquiring from the pool.
                return new InMemoryActivityContext("__pool__");
            }
        });
        this.deploymentClassLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * Build a fresh {@link VirtualThreadSbbEntityPool} from the current
     * configuration. Used both for the initial construction and to rebuild the
     * pool after {@link #stop()} shut the previous one down.
     */
    private VirtualThreadSbbEntityPool newSbbEntityPool() {
        VirtualThreadSbbEntityPool pool = new VirtualThreadSbbEntityPool(
                configuration.getSbbPoolMin(),
                configuration.getSbbPoolMax(),
                configuration.isSbbPerVirtualThread());
        pool.setContainer(this);
        return pool;
    }

    /**
     * Production P1.2 — reflectively construct the JTA transaction context
     * bound to the {@link EventRouter}.
     *
     * <p>Why reflection? {@code jainslee-core} MUST NOT depend on
     * {@code jainslee-tx} (compile-time). The two modules connect only at
     * runtime, when {@code jainslee-tx} is on the classpath.
     *
     * <ul>
     *   <li>{@code txEnabled = false} → instantiates
     *       {@code com.microjainslee.tx.NoOpTransactionManager} if available;
     *       otherwise returns {@code null} so the EventRouter skips wrapping.</li>
     *   <li>{@code txEnabled = true}  → instantiates
     *       {@code com.microjainslee.tx.JtaTransactionManager}; throws
     *       {@link IllegalStateException} at construction time (so the user
     *       sees the error at {@code new MicroSleeContainer(...)} rather than
     *       on the first event) if the JTA module is missing from the
     *       classpath.</li>
     * </ul>
     */
    private static Object createJtaTransactionContext(boolean txEnabled) {
        if (!txEnabled) {
            return createInstanceOrNull("com.microjainslee.tx.NoOpTransactionManager");
        }
        return createInstanceOrFail("com.microjainslee.tx.JtaTransactionManager");
    }

    private static Object createInstanceOrNull(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return cls.getDeclaredConstructor().newInstance();
        } catch (Throwable t) {
            // NoOpTransactionManager is best-effort. When the tx module is
            // absent the EventRouter sees null and behaves exactly like the
            // pre-P1.2 R&D code path.
            return null;
        }
    }

    private static Object createInstanceOrFail(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return cls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalStateException(
                    "MicroSleeConfiguration.txEnabled = true but the JTA module "
                            + "(com.microjainslee:jainslee-tx) is not on the classpath. "
                            + "Add it to your dependencies or set txEnabled(false).", cnfe);
        } catch (ReflectiveOperationException roe) {
            throw new IllegalStateException(
                    "Failed to instantiate JTA transaction context: " + className, roe);
        }
    }

    /**
     * Production P2.1 — bind a {@code com.microjainslee.cluster.ClusterManager}
     * instance to the container. The cluster layer is loaded reflectively
     * (this method receives the instance as {@link Object} and stores it as
     * such) so {@code jainslee-core} keeps a clean compile-time boundary
     * with {@code jainslee-cluster}. The kernel never imports the cluster
     * API; embedders that want cluster support pass the instance through
     * this seam.
     *
     * <p>Reflexive contract verified at bind time:
     * <ul>
     *   <li>The argument's runtime class MUST be exactly
     *       {@code com.microjainslee.cluster.ClusterManager} (compared via
     *       {@code Class.forName(...).isInstance(argument)}). Any other type
     *       results in an {@link IllegalArgumentException}.</li>
     *   <li>When the cluster module is on the classpath, the kernel will
     *       call {@code ClusterManager.start()} reflectively after
     *       {@link #start()} succeeds, so the JGroups transport is brought
     *       up in lock-step with the kernel. If the call fails we log a
     *       warning and leave the cluster manager unbound &mdash; the
     *       container remains usable in local mode.</li>
     * </ul>
     *
     * <p>Idempotent: binding a second instance replaces the first and
     * stops the previous one (best-effort). Passing {@code null} clears
     * the binding.
     *
     * @param clusterManager a {@code ClusterManager} instance, or
     *                       {@code null} to clear
     */
    public synchronized void bindCluster(Object clusterManager) {
        if (clusterManager == null) {
            Object previous = this.clusterManager;
            this.clusterManager = null;
            invokeStopOnClusterManager(previous);
            return;
        }
        // Class.forName("com.microjainslee.cluster.ClusterManager") — the
        // kernel does NOT depend on jainslee-cluster at compile time, so we
        // resolve the class by name. The check happens lazily so unit
        // tests that never touch the cluster module do not pay the
        // class-loading cost.
        Class<?> clusterClass;
        try {
            clusterClass = Class.forName("com.microjainslee.cluster.ClusterManager");
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(
                    "bindCluster() called but the cluster module "
                            + "(com.microjainslee:jainslee-cluster) is not on the classpath. "
                            + "Add it to your dependencies or set clusterEnabled(false).", cnfe);
        }
        if (!clusterClass.isInstance(clusterManager)) {
            throw new IllegalArgumentException(
                    "bindCluster() expected an instance of "
                            + "com.microjainslee.cluster.ClusterManager, got "
                            + clusterManager.getClass().getName());
        }
        Object previous = this.clusterManager;
        this.clusterManager = clusterManager;
        LOG.info("Bound cluster manager: {}", clusterManager.getClass().getName());
        // Stop the previous instance (best-effort). The previous one may
        // already be stopped if the embedder is rebinding after a restart.
        if (previous != null && previous != clusterManager) {
            invokeStopOnClusterManager(previous);
        }
    }

    /**
     * Production P2.2 — install a clustered
     * {@code com.microjainslee.cluster.ClusteredActivityContextNamingFacility}
     * over the in-memory facility created at construction time.
     *
     * <p>The class is loaded reflectively by name
     * ({@code com.microjainslee.cluster.ClusteredActivityContextNamingFacility})
     * so the kernel keeps its compile-time boundary with
     * {@code jainslee-cluster}. The argument's runtime type must be
     * exactly the cluster ACNF class; any other type results in
     * {@link IllegalArgumentException}.
     *
     * <p>Reflexive contract verified at bind time:
     * <ul>
     *   <li>The argument's class is checked against
     *       {@code Class.forName("com.microjainslee.cluster.ClusteredActivityContextNamingFacility").isInstance(arg)}.</li>
     *   <li>The argument's class must expose a single-arg
     *       {@code ClusterManager} constructor — that is how the
     *       cluster layer wires the facility to a specific
     *       {@code ClusterManager}. We do not invoke the constructor
     *       here (the embedder already produced the instance); we
     *       just record it.</li>
     * </ul>
     *
     * <p>Idempotent: rebinding replaces the previous facility
     * (best-effort clear on the old one if it exposes {@code clear()}).
     *
     * @param facility a {@code ClusteredActivityContextNamingFacility}
     *                 instance, or {@code null} to clear the binding
     *                 (the kernel falls back to the in-memory facility
     *                 the next time it asks for one)
     */
    public synchronized void bindActivityContextNamingFacility(Object facility) {
        if (facility == null) {
            AcnfBackend previous = this.activityContextNamingFacility;
            this.activityContextNamingFacility = new InMemoryAcnfBackend(new InMemoryActivityContextNamingFacility());
            if (previous != null) {
                try {
                    previous.clear();
                } catch (RuntimeException ignored) {
                    // best effort
                }
            }
            LOG.info("Cleared activity context naming facility; reverted to in-memory");
            return;
        }
        Class<?> acnfClass;
        try {
            acnfClass = Class.forName(
                    "com.microjainslee.cluster.ClusteredActivityContextNamingFacility");
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(
                    "bindActivityContextNamingFacility() called but the cluster module "
                            + "(com.microjainslee:jainslee-cluster) is not on the classpath. "
                            + "Add it to your dependencies.", cnfe);
        }
        if (!acnfClass.isInstance(facility)) {
            throw new IllegalArgumentException(
                    "bindActivityContextNamingFacility() expected an instance of "
                            + "com.microjainslee.cluster.ClusteredActivityContextNamingFacility, got "
                            + facility.getClass().getName());
        }
        // The cluster ACNF class is a subtype of InMemoryActivityContextNamingFacility's
        // API surface used by the kernel (bind, lookup, unbind, names), but
        // it does NOT extend InMemoryActivityContextNamingFacility. The kernel
        // field type is InMemoryActivityContextNamingFacility for the R&D
        // path, so we cannot assign the cluster facility to the same field
        // without a wrapper. Use a reflective duck-typed wrapper that
        // implements the same methods.
        this.activityContextNamingFacility = new ReflectiveAcnfBackend(
                facility, acnfClass);
        LOG.info("Bound clustered activity context naming facility: {}",
                facility.getClass().getName());
    }

    /**
     * Production P2.3 - register a distributed SBB entity pool
     * ({@code com.microjainslee.cluster.DistributedSbbEntityPool})
     * with the kernel.
     *
     * <p>The cluster class is loaded reflectively by name
     * ({@code com.microjainslee.cluster.DistributedSbbEntityPool})
     * so the kernel keeps its compile-time boundary with
     * {@code jainslee-cluster}. The argument's runtime type must be
     * exactly the cluster pool class; any other type results in
     * {@link IllegalArgumentException}.
     *
     * <p>Reflexive contract verified at bind time:
     * <ul>
     *   <li>The argument's class is checked against
     *       {@code Class.forName("com.microjainslee.cluster.DistributedSbbEntityPool").isInstance(arg)}.</li>
     *   <li>The argument's class must expose a constructor
     *       {@code (int, int, boolean, ClusterManager)} so embedders
     *       cannot pass an instance built with a divergent API.</li>
     * </ul>
     *
     * <p>Idempotent: rebinding replaces the previous pool. Passing
     * {@code null} clears the binding (the previously registered pool
     * is {@link VirtualThreadSbbEntityPool#shutdown() shut down} on
     * best effort).
     *
     * <h2>Why we do not rebind {@code sbbEntityPool}</h2>
     * The kernel field is strongly typed as
     * {@link VirtualThreadSbbEntityPool} and the cluster pool is
     * <em>not</em> a subtype (composition, not inheritance &mdash;
     * {@code VirtualThreadSbbEntityPool} is {@code final}). Java's
     * reflective {@code Method.invoke} enforces runtime type
     * compatibility, so we cannot silently swap the field through
     * reflection either. Embedders that want their events routed
     * through the distributed pool should call
     * {@link com.microjainslee.core.EventRouter#bindSbbEntityPool(VirtualThreadSbbEntityPool)}
     * themselves (with the cluster pool cast or wrapped) &mdash;
     * {@link com.microjainslee.core.EventRouter} accepts the
     * concrete supertype by design.
     *
     * <p>This method simply records the reference and exposes it via
     * {@link #getDistributedSbbPool()} so the rest of the kernel can
     * consult the cluster pool without a compile-time dependency on
     * {@code jainslee-cluster}.
     *
     * @param pool a {@code DistributedSbbEntityPool} instance, or
     *             {@code null} to clear the binding
     */
    public synchronized void bindDistributedSbbPool(Object pool) {
        Class<?> poolClass;
        try {
            poolClass = Class.forName(
                    "com.microjainslee.cluster.DistributedSbbEntityPool");
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(
                    "bindDistributedSbbPool() called but the cluster module "
                            + "(com.microjainslee:jainslee-cluster) is not on the classpath. "
                            + "Add it to your dependencies.", cnfe);
        }
        if (pool != null && !poolClass.isInstance(pool)) {
            throw new IllegalArgumentException(
                    "bindDistributedSbbPool() expected an instance of "
                            + "com.microjainslee.cluster.DistributedSbbEntityPool, got "
                            + pool.getClass().getName());
        }
        try {
            poolClass.getConstructor(int.class, int.class, boolean.class,
                    Class.forName("com.microjainslee.cluster.ClusterManager"));
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(
                    "DistributedSbbEntityPool does not expose a "
                            + "(int, int, boolean, ClusterManager) constructor", nsme);
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(
                    "ClusterManager class not on classpath while validating "
                            + "DistributedSbbEntityPool constructor", cnfe);
        }
        Object previous = this.distributedSbbEntityPool;
        this.distributedSbbEntityPool = pool;
        if (previous != null) {
            // Best effort - the embedder may have already shut it down.
            try {
                previous.getClass().getMethod("shutdown").invoke(previous);
            } catch (RuntimeException | java.lang.reflect.InvocationTargetException
                     | NoSuchMethodException | IllegalAccessException ignored) {
                // best effort
            }
        }
        if (pool != null) {
            LOG.info("Bound distributed SBB entity pool: {}",
                    pool.getClass().getName());
        } else {
            LOG.info("Cleared distributed SBB entity pool binding");
        }
    }

    /** Optional reference to a bound DistributedSbbEntityPool, or {@code null}. */
    private volatile Object distributedSbbEntityPool;

    /**
     * @return the bound {@code DistributedSbbEntityPool} instance, or
     *         {@code null} when no cluster pool has been installed.
     *         Strongly-typed callers should cast; the kernel returns
     *         {@link Object} on purpose so {@code jainslee-core} keeps
     *         its compile-time boundary with {@code jainslee-cluster}.
     */
    public Object getDistributedSbbPool() {
        return distributedSbbEntityPool;
    }

    /**
     * Production P2.1 — reflectively call {@code ClusterManager.start()}
     * on the bound instance. Best-effort: a failure is logged and the
     * container proceeds in local mode.
     */
    private void invokeStartOnClusterManager(Object mgr) {
        if (mgr == null) {
            return;
        }
        try {
            java.lang.reflect.Method start = mgr.getClass().getMethod("start");
            start.invoke(mgr);
        } catch (NoSuchMethodException nsme) {
            LOG.warn("ClusterManager has no start() method: {}", nsme.getMessage());
        } catch (ReflectiveOperationException roe) {
            LOG.warn("ClusterManager.start() failed: {}", roe.getMessage());
        }
    }

    /**
     * Production P2.1 — reflectively call {@code ClusterManager.stop()}.
     * Always best-effort: a failure is logged but never thrown, so the
     * container's {@link #stop()} path remains exception-clean.
     */
    private static void invokeStopOnClusterManager(Object mgr) {
        if (mgr == null) {
            return;
        }
        try {
            java.lang.reflect.Method stop = mgr.getClass().getMethod("stop");
            stop.invoke(mgr);
        } catch (NoSuchMethodException nsme) {
            // Fine — some embedders may pass a stub that does not need
            // explicit teardown.
        } catch (ReflectiveOperationException roe) {
            // Best-effort — embedders usually call this from a shutdown
            // hook; surfacing the error would mask the real cause.
        }
    }

    public synchronized void start() {
        if (state == State.STARTED) {
            return;
        }
        // VT-PINNING — enable JVM-level pinning trace when configured.
        // Must be set before any virtual thread work begins so the
        // trace covers the full container lifecycle.
        if (configuration.isTracePinnedThreads()) {
            System.setProperty("jdk.tracePinnedThreads", "full");
            LOG.info("VT pinning trace enabled (-Djdk.tracePinnedThreads=full)");
        }
        // The SBB entity pool's underlying executor is shut down by stop();
        // rebuild it so a stop/start round-trip yields a usable pool.
        if (sbbEntityPool == null || sbbEntityPool.isShutdown()) {
            sbbEntityPool = newSbbEntityPool();
            eventRouter.bindSbbEntityPool(sbbEntityPool);
            LOG.info("Re-created SBB entity pool after previous stop()");
        }
        // Sprint S8.5 — lazy-init the shared dedup window used by every
        // SleeEndpointImpl the container builds. The window lives for
        // the lifetime of the container; stop() does NOT destroy it, so
        // a subsequent start() can keep using it without a gap. Embedded
        // apps that want to opt out must call #disableDedupWindow() (or
        // inject a custom window via #setDedupWindow()) before start().
        if (this.dedupWindow == null && !this.dedupWindowDisabled) {
            this.dedupWindow = new com.microjainslee.core.ordering.DedupWindow(
                    60_000L, com.microjainslee.core.ordering.DedupWindow.DEFAULT_MAX_ENTRIES);
            LOG.info("DedupWindow wired: windowMs=60000 maxEntries={}",
                    com.microjainslee.core.ordering.DedupWindow.DEFAULT_MAX_ENTRIES);
        }
        CmpFieldStoreLocator.set(cmpFieldStore);
        sbbEntityPool.prewarm(sbbEntityPool.getMin());
        state = State.STARTED;
        // Sprint S6 — register the structured lifecycle logger on the
        // removal bus. Registered exactly once per start() call; the
        // stop() method unsubscribes so we never leak listeners across
        // stop/start cycles.
        if (this.sessionLifecycleLogger == null) {
            this.sessionLifecycleLogger = new SessionLifecycleLogger();
            entityRemovalBus.subscribe(this.sessionLifecycleLogger);
        }
        // Sprint S8 — lazy-create the out-of-order buffer if no embedder has
        // supplied one. Capacity 16/convergence, 30s TTL, 30s sweep. If the
        // IES dispatcher was already bound (rare but legal), push the buffer
        // into it now so non-initial SequencedEvents start being buffered
        // from this very start().
        if (this.outOfOrderBuffer == null) {
            this.outOfOrderBuffer = new OutOfOrderBuffer(16, 30_000L, 30_000L);
            LOG.info("MicroSleeContainer auto-created OutOfOrderBuffer (capacity=16, windowMs=30000, sweepMs=30000)");
        }
        Object iesDispatcherAtStart = this.iesDispatcher;
        if (iesDispatcherAtStart != null) {
            installOobIntoDispatcher(iesDispatcherAtStart);
        }
        // Production P2.1 — bring up the JGroups transport (in cluster mode)
        // after the kernel has finished its own start sequence. Bound
        // before autoDeployFromClasspathIndex() so SBBs that consult
        // ClusterManager at deploy time see a started manager.
        invokeStartOnClusterManager(this.clusterManager);
        autoDeployFromClasspathIndex();
        // GOAL 2 — activate all registered local RA endpoints.
        for (RaEndpointPort endpoint : endpointPorts.values()) {
            String name = endpoint.getRaName();
            SleeEndpointPortImpl sleeEndpoint = new SleeEndpointPortImpl(this, name);
            RaBootstrapPort bootstrap = new BootstrapPortAdapter(sleeEndpoint);
            try {
                endpoint.activate(bootstrap);
                LOG.info("Activated local RA endpoint: {}", name);
            } catch (RuntimeException re) {
                LOG.error("Failed to activate RA endpoint [{}]: {}", name, re.getMessage(), re);
            }
        }
    }

    public synchronized void stop() {
        if (state == State.STOPPED) {
            return;
        }
        for (ServiceID serviceID : serviceRegistry.snapshot().keySet()) {
            if (serviceRegistry.isActive(serviceID)) {
                serviceRegistry.stop(serviceID);
            }
        }
        // RA teardown follows spec §11.3 state machine: Active → Stopping
        // → Inactive → Unconfigured. AbstractResourceAdaptor chains
        // raUnconfigure() → unsetResourceAdaptorContext() automatically;
        // direct implementers (HttpIngressResourceAdaptor,
        // GrpcMenuResourceAdaptor) get the no-op default.
        for (RaBootstrapContextImpl context : resourceAdaptors.values()) {
            ResourceAdaptor ra = context.getResourceAdaptor();
            if (ra != null) {
                ra.raStopping();
                ra.raInactive();
                ra.raUnconfigure();
            }
        }
        resourceAdaptors.clear();
        // Perfect Core S5 — tear down S5-wired entities through their
        // state machine + endpoint, which routes the stop sequence
        // through RaEntityStateMachine (instead of skipping to
        // raUnconfigure). Idempotent and side-effect free when no S5
        // entity was registered.
        for (String raEntityName : java.util.List.copyOf(raEntities.keySet())) {
            stopRA(raEntityName);
        }
        raEntities.clear();
        sbbEntityPool.shutdown();
        timerPort.getBridge().shutdown();
        eventRouter.shutdown();
        activityContextNamingFacility.clear();
        sbbs.clear();
        entityTypesById.clear();
        CmpFieldStoreLocator.set(null);
        profileFacility.shutdown();
        // Sprint S6 — unsubscribe the structured lifecycle logger so a
        // subsequent start() registers a fresh subscriber (no double-fire).
        if (this.sessionLifecycleLogger != null) {
            entityRemovalBus.unsubscribe(this.sessionLifecycleLogger);
            this.sessionLifecycleLogger = null;
        }
        // Sprint S8 — shut down the OOB evictor executor. Best-effort:
        // a failure here would mask the real stop() cause, so we swallow.
        if (this.outOfOrderBuffer != null) {
            try {
                this.outOfOrderBuffer.shutdown();
            } catch (Throwable t) {
                LOG.warn("OutOfOrderBuffer.shutdown() threw during stop(): {}", t.getMessage(), t);
            }
            this.outOfOrderBuffer = null;
        }
        // GOAL 2 — deactivate all registered local RA endpoints in reverse order.
        java.util.List<RaEndpointPort> endpoints = new java.util.ArrayList<>(endpointPorts.values());
        java.util.Collections.reverse(endpoints);
        for (RaEndpointPort endpoint : endpoints) {
            try {
                endpoint.deactivate();
                LOG.info("Deactivated local RA endpoint: {}", endpoint.getRaName());
            } catch (RuntimeException re) {
                LOG.warn("Error deactivating RA endpoint [{}]: {}", endpoint.getRaName(), re.getMessage());
            }
        }
        // Production P2.1 — release JGroups threads + Infinispan resources
        // after the kernel has finished its own teardown. invokeStopOn... is
        // best-effort: a failure is logged but never thrown, so the
        // container's stop() path stays exception-clean.
        invokeStopOnClusterManager(this.clusterManager);
        this.clusterManager = null;
        state = State.STOPPED;
    }

    public SimpleSbbLocalObject registerSbb(String id, Sbb sbb) {
        return registerSbb(id, sbb, EventMask.ACCEPT_ALL, new ServiceID(id, "com.microjainslee", "1.0"));
    }

    /**
     * JAIN-SLEE 1.1 §8.6 — register an SBB entity with an explicit
     * {@link EventMask}. The mask is stored in the entity's
     * {@link SbbEntityState} and consulted by {@link EventRouter} on every
     * incoming event to avoid waking the SBB for irrelevant types.
     *
     * @param id   unique entity id
     * @param sbb  the SBB POJO
     * @param mask event mask; {@code null} is treated as
     *             {@link EventMask#ACCEPT_ALL}
     * @return the local object for this SBB entity
     */
    public SimpleSbbLocalObject registerSbb(String id, Sbb sbb, EventMask mask) {
        return registerSbb(id, sbb, mask, new ServiceID(id, "com.microjainslee", "1.0"));
    }

    public SimpleSbbLocalObject registerSbb(String id, Sbb sbb, ServiceID serviceID) {
        return registerSbb(id, sbb, EventMask.ACCEPT_ALL, serviceID);
    }

    /**
     * Full-fidelity register — mask + service id. This is the entry point
     * that actually constructs the {@link SimpleSbbLocalObject}; the other
     * overloads funnel here.
     */
    public SimpleSbbLocalObject registerSbb(String id, Sbb sbb, EventMask mask, ServiceID serviceID) {
        if (state != State.STARTED) {
            throw new IllegalStateException("Container must be started before registering SBBs");
        }
        if (sbbs.containsKey(id)) {
            return sbbs.get(id);
        }
        if (sbb != null && sbbTypeRegistry.isRegistered(sbb.getClass())) {
            @SuppressWarnings("unchecked")
            Class<? extends Sbb> type = (Class<? extends Sbb>) sbb.getClass();
            return acquireEntity(id, type, mask, serviceID);
        }
        return registerLegacySbb(id, sbb, mask, serviceID);
    }

    /**
     * Register a pooled SBB type for {@link #acquireEntity(String, Class)}.
     */
    public void registerSbbType(Class<? extends Sbb> type, java.util.function.Supplier<Sbb> factory) {
        registerSbbType(type, SbbTypePoolConfig.builder(factory)
                .minIdle(configuration.getSbbTypePoolMinIdle())
                .maxActive(configuration.getSbbPoolMax())
                .build());
    }

    public void registerSbbType(Class<? extends Sbb> type, SbbTypePoolConfig config) {
        if (type == null || config == null) {
            throw new IllegalArgumentException("type and config are required");
        }
        sbbTypeRegistry.register(type, config);
        LOG.info("Registered pooled SBB type {} (maxActive={})", type.getName(), config.getMaxActive());
    }

    public SimpleSbbLocalObject acquireEntity(String id, Class<? extends Sbb> type) {
        return acquireEntity(id, type, EventMask.ACCEPT_ALL,
                new ServiceID(id, "com.microjainslee", "1.0"));
    }

    public SimpleSbbLocalObject acquireEntity(String id, Class<? extends Sbb> type,
            EventMask mask, ServiceID serviceID) {
        if (state != State.STARTED) {
            throw new IllegalStateException("Container must be started before acquiring SBB entities");
        }
        if (id == null || type == null) {
            throw new IllegalArgumentException("id and type are required");
        }
        if (sbbs.containsKey(id)) {
            return sbbs.get(id);
        }
        final SbbTypePool typePool = sbbTypeRegistry.require(type);
        final EventMask effectiveMask = mask != null ? mask : typePool.getDefaultEventMask();
        final Sbb sbb = typePool.borrow();
        final boolean pooledReuse = typePool.isPooledReuse(sbb);
        final long entityId = entityIdAllocator.allocate();
        final VirtualThreadSbbEntityPool.SbbEntity entity =
                sbbEntityPool.acquire(id, entityId, sbb);
        final SbbID sbbID = new SbbID(id);
        final SimpleSbbLocalObject localObject = buildLocalObject(
                id, sbbID, sbb, entity, type, true, effectiveMask, serviceID);
        entityTypesById.put(id, type);
        activateEntity(id, serviceID, sbbID, localObject, entity, sbb, pooledReuse);
        sbbs.put(id, localObject);
        sbbRegistrationOrder.addIfAbsent(id);
        if (autoDeployInProgress) {
            descriptorRegisteredIds.add(id);
        }
        return localObject;
    }

    public void releaseEntity(String id) {
        SimpleSbbLocalObject localObject = sbbs.get(id);
        if (localObject != null && !localObject.isRemoved()) {
            localObject.remove();
        }
    }

    /**
     * Sprint S9.2 — return the {@link SimpleSbbLocalObject} registered
     * against {@code sbbId}, or {@code null} when no entity has been
     * registered with that id. Used by the cluster rehydration path
     * (DistributedSbbEntityPool.applySnapshot) to look up the freshly
     * reconstructed entity before re-attaching it to its ACIs.
     *
     * @param sbbId the SBB entity id (may be {@code null})
     * @return the local object, or {@code null}
     */
    public SimpleSbbLocalObject getSbbLocalObject(String sbbId) {
        if (sbbId == null) {
            return null;
        }
        return sbbs.get(sbbId);
    }

    public SbbTypeRegistry getSbbTypeRegistry() {
        return sbbTypeRegistry;
    }

    private SimpleSbbLocalObject registerLegacySbb(String id, Sbb sbb, EventMask mask,
            ServiceID serviceID) {
        final EventMask effectiveMask = mask != null ? mask : EventMask.ACCEPT_ALL;
        final VirtualThreadSbbEntityPool.SbbEntity entity = sbbEntityPool.acquire(id, () -> sbb);
        final SbbID sbbID = new SbbID(id);
        final SimpleSbbLocalObject localObject = buildLocalObject(
                id, sbbID, entity.getSbb(), entity, null, false, effectiveMask, serviceID);
        // §8.6 — bind the event mask onto the entity's state BEFORE returning
        // to the caller, so any subsequent routeEvent() sees the filter.
        localObject.getEntityState().setEventMask(effectiveMask);
        // SYNCHRONOUSLY bind entity id + CMP store on the SBB instance
        // BEFORE submitting to the entity thread. This ensures callers
        // can use CMP accessors immediately after registerSbb() returns,
        // without racing the async activation.
        if (sbb instanceof CmpBackedSbb) {
            CmpBackedSbb backed = (CmpBackedSbb) sbb;
            backed.setSbbEntityId(id);
            backed.setCmpFieldStore(cmpFieldStore);
        }
        entity.submit(new Runnable() {
            @Override
            public void run() {
                SimpleSbbContext ctx = new SimpleSbbContext(serviceID, localObject, sbbID, timerPort,
                        activityContextNamingFacility, profileFacility, alarmFacility);
                Sbb sbbInstance = entity.getSbb();
                try {
                    // Phase A state machine: setSbbContext -> sbbCreate -> sbbPostCreate -> sbbActivate.
                    // Falls back to the legacy direct invocation when sbbCreate throws
                    // a non-CreateException failure (e.g. a RuntimeException from user code).
                    sbbLifecycleManager.create(sbbInstance, ctx, null);
                    sbbLifecycleManager.postCreate(sbbInstance);
                    // Pre-populate CMP store on entity state so the SBB can read it
                    // through the (reflection-based) CmpAccessorInvoker during sbbLoad.
                    Map<String, Object> cmpState = cmpFieldStore.load(id);
                    localObject.getEntityState().getCmpFields().putAll(cmpState);
                    sbbLifecycleManager.activate(sbbInstance, cmpState);
                    // SbbLifecycleManager.activate() already transitions to READY; mirror
                    // the state in the per-entity state object as well.
                    localObject.getEntityState().transitionTo(SbbLifecycleManager.State.READY);
                } catch (CreateException ce) {
                    LOG.warn("SBB {} failed sbbCreate/sbbPostCreate: {}", id, ce.getMessage());
                    sbbLifecycleManager.removeEntity(sbbInstance);
                    sbbs.remove(id);
                    sbbEntityPool.release(entity);
                    cmpFieldStore.remove(id);
                    throw new RuntimeException(ce);
                } catch (RuntimeException re) {
                    LOG.error("SBB {} activation failed: {}", id, re.getMessage(), re);
                    sbbs.remove(id);
                    cmpFieldStore.remove(id);
                    throw re;
                }
            }
        });
        sbbs.put(id, localObject);
        sbbRegistrationOrder.addIfAbsent(id);
        if (autoDeployInProgress) {
            descriptorRegisteredIds.add(id);
        }
        return localObject;
    }

    private void autoDeployFromClasspathIndex() {
        autoDeployInProgress = true;
        try {
            SbbIndexLoader.SbbIndex index = SbbIndexLoader.load(deploymentClassLoader);
            if (index.isEmpty()) {
                LOG.debug("No {} entries found on classpath", SbbIndexLoader.INDEX_RESOURCE);
                return;
            }
            LOG.info("Auto-deploying from {}: {} sbb(s), {} eventType(s), {} du(s)",
                    SbbIndexLoader.INDEX_RESOURCE,
                    index.getSbbs().size(),
                    index.getEventTypes().size(),
                    index.getDeployableUnits().size());

            for (SbbIndexLoader.SbbIndexEntry entry : index.getSbbs()) {
                deploySbbEntry(entry);
            }
            for (SbbIndexLoader.DeployableUnitIndexEntry du : index.getDeployableUnits()) {
                deployDeployableUnit(du, index);
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to load " + SbbIndexLoader.INDEX_RESOURCE, ioe);
        } finally {
            autoDeployInProgress = false;
        }
    }

    private void deploySbbEntry(SbbIndexLoader.SbbIndexEntry entry) {
        if (sbbs.containsKey(entry.getName())) {
            LOG.debug("SBB {} already registered — skipping", entry.getName());
            return;
        }
        // Descriptor-based auto-deploy is best-effort: APT indexes every
        // @SbbAnnotation class, including abstract SBBs and SBBs whose
        // constructors take collaborators — those can only be registered
        // programmatically (registerSbbType with a factory). A failed
        // auto-instantiation must not abort container start.
        Sbb sbb;
        try {
            sbb = instantiateComponent(entry.getClassName(), Sbb.class);
        } catch (RuntimeException e) {
            LOG.warn("SBB {} ({}) cannot be auto-instantiated — register it "
                    + "programmatically via registerSbbType(): {}",
                    entry.getName(), entry.getClassName(), e.getMessage());
            return;
        }
        ServiceID serviceID = new ServiceID(entry.getName(), entry.getVendor(), entry.getVersion());
        registerSbb(entry.getName(), sbb, serviceID);
        LOG.info("Auto-registered SBB {} ({})", entry.getName(), entry.getClassName());
    }

    private void deployDeployableUnit(SbbIndexLoader.DeployableUnitIndexEntry du,
                                      SbbIndexLoader.SbbIndex index) {
        ServiceID serviceID = new ServiceID(du.getName(), du.getVendor(), du.getVersion());
        serviceRegistry.activate(serviceID);
        LOG.info("Activated service {} from deployable unit {}", serviceID, du.getClassName());

        for (String sbbClassName : du.getSbbs()) {
            SbbIndexLoader.SbbIndexEntry entry = findSbbEntry(index, sbbClassName);
            if (entry != null) {
                deploySbbEntry(entry);
            } else {
                Sbb sbb = instantiateComponent(sbbClassName, Sbb.class);
                String id = simpleClassName(sbbClassName);
                registerSbb(id, sbb, serviceID);
                LOG.info("Auto-registered DU SBB {} ({})", id, sbbClassName);
            }
        }

        for (String raClassName : du.getRas()) {
            bootstrapResourceAdaptor(raClassName, du.getName());
        }
    }

    private static SbbIndexLoader.SbbIndexEntry findSbbEntry(SbbIndexLoader.SbbIndex index, String className) {
        for (SbbIndexLoader.SbbIndexEntry entry : index.getSbbs()) {
            if (entry.getClassName().equals(className)) {
                return entry;
            }
        }
        return null;
    }

    private static String simpleClassName(String className) {
        int dot = className.lastIndexOf('.');
        return dot >= 0 ? className.substring(dot + 1) : className;
    }

    public RaBootstrapContextImpl bootstrapResourceAdaptor(String raClassName, String entityName) {
        if (resourceAdaptors.containsKey(entityName)) {
            return resourceAdaptors.get(entityName);
        }
        ResourceAdaptor ra = instantiateComponent(raClassName, ResourceAdaptor.class);
        RaBootstrapContextImpl context = new RaBootstrapContextImpl(this, entityName);
        context.setResourceAdaptor(ra);
        ra.setResourceAdaptorContext(context);
        ra.raConfigure();
        ra.raActive();
        resourceAdaptors.put(entityName, context);
        LOG.info("Bootstrapped resource adaptor {} as entity {}", raClassName, entityName);
        return context;
    }

    // ──────────────────────────────────────────────────────────
    // Perfect Core S5 — full RA wiring with state machine + endpoint + context
    // ──────────────────────────────────────────────────────────

    /**
     * RA-entity record stored in {@link #raEntities}. Holds the
     * {@link com.microjainslee.ra.ResourceAdaptorContextImpl} plus the
     * state machine and endpoint so {@link #stopRA(String)} can drive
     * the spec-mandated Active -> Stopping -> Inactive sequence.
     */
    private final ConcurrentHashMap<String, RaEntity> raEntities = new ConcurrentHashMap<>();

    // ──────────────────────────────────────────────────────────
    // GOAL 2 — 3-port local RA registration
    // ──────────────────────────────────────────────────────────

    /** Map of RA entity name → {@link RaEndpointPort} registered via {@link #registerRa(RaEndpointPort, RaCommandPort)}. */
    private final ConcurrentHashMap<String, RaEndpointPort> endpointPorts = new ConcurrentHashMap<>();

    /** Map of event class → SBB entity name for convergent event-to-SBB routing. */
    private final ConcurrentHashMap<Class<? extends SleeEvent>, String> eventToSbbMap = new ConcurrentHashMap<>();

    // ──────────────────────────────────────────────────────────
    // P0 — per-deployment in-flight Activity Context tracking
    // ──────────────────────────────────────────────────────────

    /** deploymentId → (acId → true) for in-flight AC counting. */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Boolean>> byDeployment =
            new ConcurrentHashMap<>();

    /**
     * P0 — count the number of in-flight activity contexts created under
     * the given deployment id.
     *
     * @param deploymentId the deployment identifier (must not be {@code null})
     * @return number of in-flight ACs, or 0 if the deployment is unknown
     */
    public int countInFlightByDeploymentId(String deploymentId) {
        if (deploymentId == null) {
            return 0;
        }
        ConcurrentHashMap<String, Boolean> acs = byDeployment.get(deploymentId);
        return acs == null ? 0 : acs.size();
    }

    /**
     * P0 — track an activity context under the given deployment id.
     * Called from {@link #createActivityContext(String)} when a deployment
     * context is available.
     *
     * @param acId         the activity context id (name)
     * @param deploymentId the deployment identifier
     */
    public void setDeploymentId(String acId, String deploymentId) {
        if (acId == null || deploymentId == null) {
            return;
        }
        byDeployment.computeIfAbsent(deploymentId,
                k -> new ConcurrentHashMap<>()).put(acId, Boolean.TRUE);
    }

    /**
     * P0 — remove an activity context from its deployment tracking.
     * Called when an AC is ended / unbound.
     *
     * @param acId         the activity context id
     * @param deploymentId the deployment identifier
     */
    public void clearDeploymentId(String acId, String deploymentId) {
        if (acId == null || deploymentId == null) {
            return;
        }
        ConcurrentHashMap<String, Boolean> acs = byDeployment.get(deploymentId);
        if (acs != null) {
            acs.remove(acId);
            if (acs.isEmpty()) {
                byDeployment.remove(deploymentId);
            }
        }
    }

    /**
     * P0 — return the set of all deployment ids currently tracked.
     */
    public java.util.Set<String> getTrackedDeploymentIds() {
        return java.util.Collections.unmodifiableSet(byDeployment.keySet());
    }

    /**
     * GOAL 2 — register a local Resource Adaptor via the 3-port contract.
     *
     * <p>The RA's {@link RaEndpointPort#activate(RaBootstrapPort)} will be called
     * during {@link #start()} (or immediately if the container is already started)
     * and {@link RaEndpointPort#deactivate()} during {@link #stop()}.
     *
     * @param endpoint the RA endpoint port (lifecycle owner)
     * @param command  the RA command port (used by SBBs to send outbound commands)
     */
    public void registerRa(RaEndpointPort endpoint, RaCommandPort command) {
        if (endpoint == null || command == null) {
            throw new IllegalArgumentException("endpoint and command are required");
        }
        String name = endpoint.getRaName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("RaEndpointPort.getRaName() must return a non-blank name");
        }
        endpointPorts.put(name, endpoint);
        raCommandPorts.put(name, command);
        LOG.info("Registered local RA: {}", name);
        // If the container is already started, activate immediately.
        if (state == State.STARTED) {
            SleeEndpointPortImpl sleeEndpoint = new SleeEndpointPortImpl(this, name);
            RaBootstrapPort bootstrap = new BootstrapPortAdapter(sleeEndpoint);
            try {
                endpoint.activate(bootstrap);
                LOG.info("Activated local RA endpoint (hot-register): {}", name);
            } catch (RuntimeException re) {
                LOG.error("Failed to activate RA endpoint [{}]: {}", name, re.getMessage(), re);
            }
        }
    }

    /**
     * GOAL 2 — map an event type to an SBB entity name for convergent
     * event routing.
     *
     * @param eventType the event class to map
     * @param sbbName   the SBB entity name that handles this event type
     */
    public void mapEventToSbb(Class<? extends SleeEvent> eventType, String sbbName) {
        if (eventType == null || sbbName == null) {
            throw new IllegalArgumentException("eventType and sbbName are required");
        }
        eventToSbbMap.put(eventType, sbbName);
        LOG.info("Mapped event {} -> SBB {}", eventType.getSimpleName(), sbbName);
    }

    /**
     * Perfect Core S5 — register a {@link ResourceAdaptor} with full
     * lifecycle wiring per JAIN SLEE 1.1 §12.4.
     *
     * <p>Lifecycle:
     * <pre>
     *   1. create state machine (INACTIVE)
     *   2. create SleeEndpointImpl with full validation
     *   3. create ResourceAdaptorContextImpl with all six facilities
     *   4. ra.setResourceAdaptorContext(ctx) + ra.raConfigure()
     *   5. stateMachine.activate() -> ACTIVE -> ra.raActive()
     * </pre>
     *
     * <p>Stopping:
     * <pre>
     *   1. stateMachine.deactivate() -> STOPPING -> ra.raStopping()
     *   2. RA drains, then calls endpoint.stopComplete()
     *   3. stateMachine.stopComplete() -> INACTIVE -> ra.raInactive()
     * </pre>
     *
     * <p>Both the legacy {@link #bootstrapResourceAdaptor(String, String)}
     * (reflection-based, used by the deployable-unit index) and the
     * modern {@link #registerResourceAdaptor(String, ResourceAdaptor)}
     * are kept. The modern path is the one embedders should use for
     * new code.
     *
     * @param raEntityName unique entity name (used as the ACNF key prefix
     *                     and the resourceAdaptor index key)
     * @param ra           the resource adaptor instance
     * @return the wired {@link com.microjainslee.ra.ResourceAdaptorContextImpl}
     */
    public synchronized com.microjainslee.ra.ResourceAdaptorContextImpl registerResourceAdaptor(
            String raEntityName, ResourceAdaptor ra) {
        if (state != State.STARTED) {
            throw new IllegalStateException(
                    "Container must be started before registering RAs (state=" + state + ")");
        }
        if (raEntityName == null || raEntityName.isEmpty()) {
            throw new IllegalArgumentException("raEntityName is required");
        }
        if (ra == null) {
            throw new IllegalArgumentException("ra is required");
        }
        if (raEntities.containsKey(raEntityName)) {
            RaEntity existing = raEntities.get(raEntityName);
            return existing.context();
        }

        // 1+2+3 — build state machine, endpoint, and context through the
        // kernel-side factory. ResourceAdaptorContextBuilder wires
        // every facility against the live container so the RA can
        // actually exercise its spec surface.
        com.microjainslee.core.ra.ResourceAdaptorContextBuilder.Built built =
                com.microjainslee.core.ra.ResourceAdaptorContextBuilder.build(
                        this, ra, raEntityName);
        com.microjainslee.ra.ResourceAdaptorContextImpl ctx = built.context();

        // 4 — bind RA into context, then run raConfigure().
        ra.setResourceAdaptorContext(ctx);
        try {
            ra.raConfigure();
        } catch (RuntimeException re) {
            LOG.error("raConfigure() failed for [{}]: {}", raEntityName, re.getMessage(), re);
            throw re;
        }

        // 5 — activate (INACTIVE -> ACTIVE -> raActive()).
        built.stateMachine().activate();

        raEntities.put(raEntityName, new RaEntity(ra, ctx, built.stateMachine(), built.endpoint()));
        // GOAL 4 — register a RaCommandPort so SBBs can @InjectRa it
        registerRaCommandPort(raEntityName, new SimpleRaCommandPort(raEntityName));
        LOG.info("Registered resource adaptor entity [{}] (S5 wiring)", raEntityName);
        return ctx;
    }

    /**
     * Perfect Core S5 — drive the spec-mandated Stop sequence for a
     * registered RA. Idempotent: calling on an already-stopped entity
     * is a no-op.
     */
    public synchronized void stopRA(String raEntityName) {
        if (raEntityName == null) return;
        RaEntity entry = raEntities.remove(raEntityName);
        if (entry == null) {
            LOG.debug("stopRA({}) -- entity not registered, ignoring", raEntityName);
            return;
        }
        RaEntityStateMachine.State current = entry.stateMachine().getState();
        if (current == RaEntityStateMachine.State.INACTIVE) {
            LOG.debug("stopRA({}) -- already INACTIVE, ignoring", raEntityName);
            return;
        }
        if (current == RaEntityStateMachine.State.ACTIVE) {
            entry.stateMachine().deactivate(); // ACTIVE -> STOPPING -> raStopping()
        }
        // STOPPING -> INACTIVE -> raInactive() (synchronous — no
        // real in-flight events to drain in R&D; production path
        // would await the RA's async stopComplete()).
        if (entry.stateMachine().getState() == RaEntityStateMachine.State.STOPPING) {
            entry.stateMachine().stopComplete();
        }
        try {
            entry.ra().raUnconfigure();
        } catch (RuntimeException re) {
            LOG.warn("raUnconfigure() failed for [{}]: {}", raEntityName, re.getMessage(), re);
        }
        entry.ra().unsetResourceAdaptorContext();
        // GOAL 4 — unregister the command port so stale ports don't linger
        raCommandPorts.remove(raEntityName);
        LOG.info("Stopped resource adaptor entity [{}] (S5 wiring)", raEntityName);
    }

    /** @return the S5-wired context for an entity, or {@code null} when unknown. */
    public com.microjainslee.ra.ResourceAdaptorContextImpl getRaContext(String raEntityName) {
        RaEntity entry = raEntities.get(raEntityName);
        return entry != null ? entry.context() : null;
    }

    /** @return the set of S5-wired RA entity names. */
    public java.util.Set<String> getRegisteredRaEntities() {
        return java.util.Collections.unmodifiableSet(raEntities.keySet());
    }

    /** Internal record holding the S5-wired objects for a single RA entity. */
    private record RaEntity(ResourceAdaptor ra,
                            com.microjainslee.ra.ResourceAdaptorContextImpl context,
                            com.microjainslee.ra.RaEntityStateMachine stateMachine,
                            com.microjainslee.ra.SleeEndpointImpl endpoint) {}

    private <T> T instantiateComponent(String className, Class<T> expectedType) {
        try {
            Class<?> clazz = Class.forName(className, true, deploymentClassLoader);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (!expectedType.isInstance(instance)) {
                throw new IllegalStateException(className + " is not a " + expectedType.getSimpleName());
            }
            return expectedType.cast(instance);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate " + className, e);
        }
    }

    public InMemoryActivityContext createActivityContext(String name) {
        // §7 — pull from the JCTools-backed ACI pool when available. The
        // pooled instances come pre-allocated from a placeholder name, so
        // we transparently substitute the caller's name. If the pool is
        // exhausted (created >= max) the factory allocates a fresh one.
        InMemoryActivityContext aci = aciPool.acquire();
        // Re-bind to the caller's name. The pool supplies a name field
        // that is final, so we use a dedicated InMemoryActivityContext if
        // we ever need to preserve the original; here the pool's factory
        // uses a placeholder that callers always overwrite via the
        // naming facility anyway, so re-binding is a no-op for the name
        // field but we still rebind into the naming facility.
        activityContextNamingFacility.bind(name, aci);
        return aci;
    }

    /**
     * @return the JCTools-backed SBB object pool. Exposed for advanced
     *         callers who want to bypass the entity-pool path and recycle
     *         SBB instances themselves (e.g. benchmarks).
     */
    public SbbObjectPool getSbbObjectPool() {
        return sbbObjectPool;
    }

    /**
     * @return the JCTools-backed Activity Context pool.
     */
    public ActivityContextPool getAciPool() {
        return aciPool;
    }

    public void attach(String activityContextName, SbbLocalObject sbbLocalObject) {
        ActivityContextInterface aci = activityContextNamingFacility.lookup(activityContextName);
        if (aci == null) {
            throw new IllegalArgumentException("Unknown activity context: " + activityContextName);
        }
        if (!(aci instanceof InMemoryActivityContext)) {
            throw new IllegalArgumentException("Unsupported activity context type: " + aci.getClass());
        }
        InMemoryActivityContext activityContext = (InMemoryActivityContext) aci;
        SbbTransactionContext transaction =
                ActivityContextTransactionRegistry.currentFor(activityContext);
        if (transaction != null) {
            transaction.recordAttach(sbbLocalObject);
            transaction.recordTimerBind(sbbLocalObject);
            return;
        }
        activityContext.attachImmediate(sbbLocalObject);
        timerPort.getBridge().bindActivityContext(sbbLocalObject, aci);
    }

    private void detachFromAllActivityContexts(SbbLocalObject sbbLocalObject) {
        for (ActivityContextInterface aci : activityContextNamingFacility.getBoundContexts()) {
            if (aci instanceof InMemoryActivityContext) {
                ((InMemoryActivityContext) aci).detachImmediate(sbbLocalObject);
            }
        }
        timerPort.getBridge().unbindActivityContext(sbbLocalObject);
    }

    public void setInitialEventSelectorCustomizer(InitialEventSelectorCustomizer customizer) {
        this.initialEventSelectorCustomizer = customizer;
    }

    /**
     * Sprint S8 — install (or replace) the {@link OutOfOrderBuffer} used
     * by the IES dispatcher. If an IES dispatcher is already bound,
     * the buffer is pushed into it immediately via reflection. Pass
     * {@code null} to disable OOB (events will be dropped per spec
     * §7.5.5 again).
     */
    public void setOutOfOrderBuffer(OutOfOrderBuffer buffer) {
        OutOfOrderBuffer previous = this.outOfOrderBuffer;
        this.outOfOrderBuffer = buffer;
        if (previous != null && previous != buffer) {
            try {
                previous.shutdown();
            } catch (Throwable t) {
                LOG.warn("OutOfOrderBuffer.shutdown() on swap threw: {}", t.getMessage(), t);
            }
        }
        Object dispatcher = this.iesDispatcher;
        if (dispatcher != null) {
            installOobIntoDispatcher(dispatcher);
        }
        LOG.info("MicroSleeContainer OutOfOrderBuffer: {}",
                buffer == null ? "DISABLED" : "ENABLED");
    }

    /** Currently-installed OutOfOrderBuffer (may be {@code null}). */
    public OutOfOrderBuffer getOutOfOrderBuffer() {
        return outOfOrderBuffer;
    }

    // ──────────────────────────────────────────────────────────
    // Sprint S8.5 — shared dedup window for SleeEndpointImpl
    // ──────────────────────────────────────────────────────────

    /**
     * Sprint S8.5 — accessor for the kernel-managed dedup window.
     * Returns {@code null} when no window is wired (either the default
     * has been disabled or {@link #start()} has not yet run).
     *
     * <p>{@link com.microjainslee.core.ra.ResourceAdaptorContextBuilder}
     * consults this on every {@code build(...)} and, when a window is
     * present, wires it into the freshly-built {@code SleeEndpointImpl}
     * via {@code withDedupCheck(...)} + {@code withDedupHitCount(...)}.
     * Embedders that build the endpoint by hand can call
     * {@code endpoint.withDedupCheck(...)} themselves and point at the
     * same window instance for shared state.</p>
     */
    public com.microjainslee.core.ordering.DedupWindow getDedupWindow() {
        return dedupWindow;
    }

    /**
     * Sprint S8.5 — install a custom dedup window. Pass {@code null}
     * before {@link #start()} to disable the dedup default. Calling
     * after {@link #start()} replaces the window in-place;
     * already-built {@code SleeEndpointImpl}s keep the reference they
     * captured at build-time, so the swap is mostly useful for tests.
     */
    public void setDedupWindow(com.microjainslee.core.ordering.DedupWindow window) {
        this.dedupWindow = window;
        if (window == null) {
            this.dedupWindowDisabled = true;
        } else {
            this.dedupWindowDisabled = false;
        }
        LOG.info("MicroSleeContainer DedupWindow: {}", window == null ? "DISABLED" : "ENABLED");
    }

    /**
     * Sprint S8.5 — convenience: skip the dedup window entirely. Must
     * be called before {@link #start()} to take effect; after that the
     * window is already constructed and the flag is ignored.
     */
    public void disableDedupWindow() {
        this.dedupWindowDisabled = true;
        this.dedupWindow = null;
        LOG.info("MicroSleeContainer DedupWindow: DISABLED (opt-out)");
    }

    /**
     * Sprint S8.5 — reflectively push the OOB + EventRouter references
     * into the bound IES dispatcher. Both setters are optional — a
     * missing setter is logged but does not throw, so an older IES
     * class still works (the OOB is then simply unused).
     */
    private void installOobIntoDispatcher(Object dispatcher) {
        if (dispatcher == null) return;
        try {
            java.lang.reflect.Method setOob = dispatcher.getClass()
                    .getMethod("setOutOfOrderBuffer", OutOfOrderBuffer.class);
            setOob.invoke(dispatcher, this.outOfOrderBuffer);
        } catch (NoSuchMethodException nsme) {
            LOG.debug("IES dispatcher {} does not expose setOutOfOrderBuffer — OOB unwired",
                    dispatcher.getClass().getName());
        } catch (ReflectiveOperationException roe) {
            LOG.warn("Failed to install OutOfOrderBuffer into IES dispatcher: {}",
                    roe.getMessage(), roe);
        }
        try {
            java.lang.reflect.Method setErp = dispatcher.getClass()
                    .getMethod("setEventRouterPort", EventRouter.class);
            setErp.invoke(dispatcher, this.eventRouter);
        } catch (NoSuchMethodException nsme) {
            LOG.debug("IES dispatcher {} does not expose setEventRouterPort — drain re-route disabled",
                    dispatcher.getClass().getName());
        } catch (ReflectiveOperationException roe) {
            LOG.warn("Failed to install EventRouter into IES dispatcher: {}",
                    roe.getMessage(), roe);
        }
    }

    /**
     * Perfect Core S3 — bind an {@code com.microjainslee.core.ies.InitialEventSelectorDispatcher}
     * so the {@link EventRouter} routes events through Initial Event Selection
     * (convergence-name lookup) instead of allocating a fresh entity per event.
     *
     * <p>The dispatcher is typed as {@link Object} so the kernel does not pull
     * in a compile-time edge to the {@code ies} package. The runtime contract
     * is the same as
     * {@link EventRouter#bindInitialEventSelectorDispatcher(Object)}:
     * class name MUST equal {@code com.microjainslee.core.ies.InitialEventSelectorDispatcher}
     * and MUST expose {@code String resolveTarget(Object, ActivityContextInterface, Class)}.
     *
     * <p>Idempotent: rebinding replaces the previous dispatcher. Passing
     * {@code null} clears the binding and restores the legacy
     * allocate-per-event behaviour.
     */
    public void setInitialEventSelectorDispatcher(Object dispatcher) {
        if (dispatcher != null
                && !"com.microjainslee.core.ies.InitialEventSelectorDispatcher"
                        .equals(dispatcher.getClass().getName())) {
            throw new IllegalArgumentException(
                    "IES dispatcher must be com.microjainslee.core.ies.InitialEventSelectorDispatcher, got: "
                            + dispatcher.getClass().getName());
        }
        eventRouter.bindInitialEventSelectorDispatcher(dispatcher);
        // Sprint S6 — wire IES cleanup via the removal bus (closes GAP-SR-7).
        // We subscribe a fresh IesCleanupAdapter for every new dispatcher so
        // rebinding swaps the cleanup chain atomically.
        if (dispatcher != null) {
            this.iesDispatcher = dispatcher;
            try {
                entityRemovalBus.subscribe(
                        new VirtualThreadSbbEntityPool.IesCleanupAdapter(
                                (com.microjainslee.core.ies.InitialEventSelectorDispatcher) dispatcher));
            } catch (ClassCastException cce) {
                // Defensive — should not happen given the class-name check above.
                LOG.warn("IES dispatcher type cast failed: {}", cce.getMessage());
            }
        } else {
            this.iesDispatcher = null;
        }
        // Sprint S8 — now that the dispatcher is bound, push the
        // OutOfOrderBuffer + EventRouter into it so non-initial
        // SequencedEvents are buffered (S8.3) and drained on entity
        // allocation (S8.4). Reflective because MicroSleeContainer does
        // not compile-depend on the IES setters.
        if (dispatcher != null) {
            installOobIntoDispatcher(dispatcher);
        }
    }

    /**
     * GOAL 2 — create an IES dispatcher whose entity pool is backed by
     * this container: entities are created through {@link #acquireEntity}
     * so they get the full lifecycle (SbbContext, sbbCreate/sbbActivate,
     * CMP load, {@code @InjectRa} injection, removal-bus cleanup). The
     * dispatcher is bound via {@link #setInitialEventSelectorDispatcher}
     * before being returned.
     *
     * <p>Prefer this over hand-rolling a
     * {@link InitialEventSelectorDispatcher.SbbEntityPool} adapter —
     * adapters that allocate raw pool entities bypass the container
     * lifecycle, so the resolved entity never appears in
     * {@link #getSbbLocalObject(String)} and cannot be attached to an
     * activity context.
     */
    public InitialEventSelectorDispatcher createIesDispatcher() {
        InitialEventSelectorDispatcher dispatcher =
                new InitialEventSelectorDispatcher(new ContainerBackedIesPool());
        setInitialEventSelectorDispatcher(dispatcher);
        return dispatcher;
    }

    /** Container-lifecycle-backed IES pool: {@code allocateNew → acquireEntity}. */
    private final class ContainerBackedIesPool
            implements InitialEventSelectorDispatcher.SbbEntityPool {
        @Override
        public String allocateNew(Class<?> sbbClass) {
            Class<? extends Sbb> typed = sbbClass.asSubclass(Sbb.class);
            String entityId = sbbClass.getSimpleName() + "#" + entityIdAllocator.allocate();
            acquireEntity(entityId, typed);
            return entityId;
        }

        @Override
        public boolean contains(String entityId) {
            SimpleSbbLocalObject localObject = sbbs.get(entityId);
            return localObject != null && !localObject.isRemoved();
        }

        @Override
        public void onEntityRemoved(String entityId, Consumer<String> callback) {
            // Intentionally a no-op: setInitialEventSelectorDispatcher
            // already subscribes an IesCleanupAdapter on the
            // EntityRemovalBus, which calls removeConvergencesFor(entityId)
            // on every removal. Registering per-entity callbacks here would
            // double-clean and leak one subscriber per entity.
        }
    }

    // ───────────────────────────────────────────────────────────────
    // Sprint S6 — Observability & Removal Notification API
    // ───────────────────────────────────────────────────────────────

    /**
     * Register a subscriber that will be invoked synchronously every time
     * an SBB entity is removed. Subscribers MUST be fast and MUST NOT call
     * back into the container (deadlock risk).
     */
    public void addEntityRemovalListener(Consumer<EntityRemovalEvent> listener) {
        entityRemovalBus.subscribe(listener);
    }

    /**
     * Unregister a previously-added removal listener. Best-effort no-op
     * when {@code listener} was never registered.
     */
    public void removeEntityRemovalListener(Consumer<EntityRemovalEvent> listener) {
        entityRemovalBus.unsubscribe(listener);
    }

    /**
     * Total removal events ever published by this container. Monotonic,
     * never reset. Mirrors {@link EntityRemovalBus#getPublishCount()} so
     * embedders that only see the {@code MicroSleeContainer} handle can
     * still observe the metric.
     */
    public long getEntityRemovalCount() {
        return entityRemovalCounter.get();
    }

    /**
     * Direct access to the underlying {@link EntityRemovalBus} for embedders
     * that want to attach a subscriber without going through the
     * {@code addEntityRemovalListener} convenience method.
     */
    public EntityRemovalBus getEntityRemovalBus() {
        return entityRemovalBus;
    }

    /**
     * Sprint S6 — capture the IES convergence name that points at
     * {@code entityId} (if any). Returns {@code null} when the IES
     * dispatcher has not been bound, or when the entity has no
     * convergence mapping.
     *
     * <p>Implemented via reflection to keep {@code MicroSleeContainer}
     * free of a compile-time edge to {@code com.microjainslee.core.ies}.
     */
    private String captureConvergenceKeyFor(String entityId) {
        Object dispatcher = this.iesDispatcher;
        if (dispatcher == null || entityId == null) {
            return null;
        }
        try {
            java.lang.reflect.Method m = dispatcher.getClass().getMethod(
                    "getConvergenceKeyFor", String.class);
            Object value = m.invoke(dispatcher, entityId);
            return value instanceof String ? (String) value : null;
        } catch (ReflectiveOperationException roe) {
            // IES dispatcher missing the method (e.g. stub or test mock).
            return null;
        }
    }

    /**
     * Sprint S6 — map a {@link SimpleSbbLocalObject.RemovalCause} into the
     * finer-grained {@link RemovalReason} carried on the bus.
     */
    private static RemovalReason resolveRemovalReason(SimpleSbbLocalObject obj) {
        if (obj == null) {
            return RemovalReason.SBB_SELF_REMOVE;
        }
        SimpleSbbLocalObject.RemovalCause cause = obj.getRemovalCause();
        if (cause == null) {
            return RemovalReason.SBB_SELF_REMOVE;
        }
        switch (cause) {
            case TIMER:            return RemovalReason.TIMER_EXPIRED;
            case SELF:             return RemovalReason.SBB_SELF_REMOVE;
            case CASCADE:          return RemovalReason.CASCADE_CHILD;
            case HOT_REDEPLOY:     return RemovalReason.HOT_REDEPLOY;
            case OPERATOR:         return RemovalReason.OPERATOR;
            case EXCEPTION_ROLLBACK: return RemovalReason.EXCEPTION_ROLLBACK;
            default:               return RemovalReason.SBB_SELF_REMOVE;
        }
    }

    public void routeEvent(SleeEvent event, ActivityContextInterface aci) {
        if (aci instanceof InMemoryActivityContext) {
            InMemoryActivityContext activityContext = (InMemoryActivityContext) aci;
            // GOAL 2 — an explicit mapEventToSbb() mapping wins, and is
            // honoured even when other SBBs already share this ACI (a
            // dialog ACI may carry ProxySbb while an ICE event still has
            // to reach IceNegotiationSbb).
            boolean mappingHandled = ensureMappedSbbAttached(event, activityContext);
            if (!mappingHandled && activityContext.getAttachedSbbs().isEmpty()) {
                attachRootSbbViaInitialEventSelector(event, activityContext);
            }
        }
        eventRouter.routeEvent(event, aci);
    }

    /**
     * GOAL 2 — consult {@link #eventToSbbMap} for this event type (walking
     * up the event class hierarchy) and make sure the mapped SBB entity is
     * attached to the activity context.
     *
     * <p>Resolution order for the mapped name:
     * <ol>
     *   <li>an already-registered entity with that exact id, else</li>
     *   <li>a pooled SBB type registered via {@link #registerSbbType};
     *       the target entity is then resolved through the IES dispatcher
     *       (convergence-name routing) when one is bound, or a
     *       deterministic per-activity entity id otherwise.</li>
     * </ol>
     *
     * @return {@code true} when a mapping existed for this event type —
     *         the legacy fallback must NOT run in that case, even if the
     *         IES decided to drop the event (spec §7.5.5).
     */
    private boolean ensureMappedSbbAttached(SleeEvent event,
            InMemoryActivityContext activityContext) {
        String sbbName = resolveMappedSbbName(event.getClass());
        if (sbbName == null) {
            return false;
        }
        SimpleSbbLocalObject target = sbbs.get(sbbName);
        if (target == null) {
            Class<? extends Sbb> type = sbbTypeRegistry.findTypeByName(sbbName);
            if (type == null) {
                LOG.warn("mapEventToSbb points at [{}] but no entity or registered "
                        + "SBB type matches — {} not routed via mapping",
                        sbbName, event.getClass().getSimpleName());
                return true;
            }
            // The mapping guarantees delivery to an SBB *of this type*. If
            // the application already attached one (manual acquireEntity +
            // attach — the pre-wiring style), the mapping is satisfied;
            // creating a second entity here would double-deliver every event
            // and bypass the app's own factory collaborators.
            for (SbbLocalObject attached : activityContext.getAttachedSbbs()) {
                if (attached instanceof SimpleSbbLocalObject simple
                        && !simple.isRemoved()
                        && simple.getSbb() != null
                        && type.isInstance(simple.getSbb())) {
                    return true;
                }
            }
            String entityId = resolveMappedEntityId(event, activityContext, type);
            if (entityId == null) {
                // IES dropped the event (non-initial, no matching entity).
                return true;
            }
            target = sbbs.get(entityId);
            if (target == null) {
                target = acquireEntity(entityId, type);
            }
        }
        if (target != null && !target.isRemoved()) {
            activityContext.attachImmediate(target);
            timerPort.getBridge().bindActivityContext(target, activityContext);
        }
        return true;
    }

    /** Walk the event class hierarchy against {@link #eventToSbbMap}. */
    private String resolveMappedSbbName(Class<?> eventType) {
        if (eventToSbbMap.isEmpty()) {
            return null;
        }
        for (Class<?> t = eventType; t != null && t != Object.class; t = t.getSuperclass()) {
            String name = eventToSbbMap.get(t);
            if (name != null) {
                return name;
            }
        }
        return null;
    }

    /**
     * Resolve the target entity id for a mapped SBB type: IES dispatcher
     * (convergence routing) when bound, else one deterministic entity per
     * (SBB type, activity) pair.
     */
    private String resolveMappedEntityId(SleeEvent event,
            InMemoryActivityContext activityContext, Class<? extends Sbb> type) {
        Object dispatcher = this.iesDispatcher;
        if (dispatcher instanceof InitialEventSelectorDispatcher ies) {
            return ies.resolveTarget(event, activityContext, type);
        }
        return type.getSimpleName() + "/" + activityContext.getActivityContextName();
    }

    private void attachRootSbbViaInitialEventSelector(SleeEvent event,
            InMemoryActivityContext activityContext) {
        InitialEventSelector selector = new DefaultInitialEventSelector(event, activityContext);
        InitialEventSelectorCustomizer customizer = initialEventSelectorCustomizer;
        if (customizer != null) {
            customizer.customize(selector);
        }
        if (!selector.isInitialEvent()) {
            return;
        }
        String rootSbbId = selector.getRootSbbId();
        SimpleSbbLocalObject root = rootSbbId != null ? sbbs.get(rootSbbId) : null;
        if (root == null) {
            // Deterministic fallback: first *registered* SBB whose event
            // mask accepts this event (registration order — never the
            // arbitrary ConcurrentHashMap iteration order).
            root = firstMaskAcceptingSbb(event);
        }
        if (root != null) {
            activityContext.attachImmediate(root);
            timerPort.getBridge().bindActivityContext(root, activityContext);
        } else if (!sbbs.isEmpty()) {
            LOG.debug("No registered SBB accepts {} — event not attached to any root",
                    event.getClass().getSimpleName());
        }
    }

    /**
     * First registered, still-live SBB entity whose {@link EventMask}
     * accepts {@code event}. Prunes stale registration-order entries as a
     * side effect.
     */
    private SimpleSbbLocalObject firstMaskAcceptingSbb(SleeEvent event) {
        SimpleSbbLocalObject descriptorFallback = null;
        for (String id : sbbRegistrationOrder) {
            SimpleSbbLocalObject candidate = sbbs.get(id);
            if (candidate == null || candidate.isRemoved()) {
                sbbRegistrationOrder.remove(id);
                descriptorRegisteredIds.remove(id);
                continue;
            }
            EventMask mask = candidate.getEntityState().getEventMask();
            if (mask == EventMask.ACCEPT_ALL || (mask != null && mask.matches(event))) {
                // GOAL 3 — programmatic registration outranks descriptor-based
                // (classpath sbb-index) registration.
                if (!descriptorRegisteredIds.contains(id)) {
                    return candidate;
                }
                if (descriptorFallback == null) {
                    descriptorFallback = candidate;
                }
            }
        }
        return descriptorFallback;
    }

    public ClassLoader createDeploymentClassLoader(File deploymentDirectory) throws MalformedURLException {
        if (deploymentDirectory == null) {
            throw new IllegalArgumentException("deploymentDirectory is required");
        }
        URL url = deploymentDirectory.toURI().toURL();
        deploymentClassLoader = new URLClassLoader(new URL[] { url },
                Thread.currentThread().getContextClassLoader());
        return deploymentClassLoader;
    }

    public State getState() {
        return state;
    }

    public ClassLoader getDeploymentClassLoader() {
        return deploymentClassLoader;
    }

    public EventRouter getEventRouter() {
        return eventRouter;
    }

    public TimerPort getTimerPort() {
        return timerPort;
    }

    /**
     * Perfect Core S5 — return the {@link SleeTimerSchedulerBridge}
     * backing the embedded {@link TimerPort}. RAs that need the
     * bridge directly (e.g. when building a {@link
     * com.microjainslee.api.TimerFacility}) call this instead of
     * unwrapping the port themselves.
     */
    public SleeTimerSchedulerBridge getTimerBridge() {
        return timerPort != null ? timerPort.getBridge() : null;
    }

    public AcnfBackend getActivityContextNamingFacility() {
        return activityContextNamingFacility;
    }

    /**
     * Sprint S9.4 — return the set of ACNF names whose bound
     * {@link ActivityContextInterface} currently has {@code sbbId}
     * attached to it.
     *
     * <p>Used by the cluster snapshot path (S9.1) and the rehydration
     * path (S7.3) to round-trip the set of ACIs an SBB entity was
     * attached to across the wire.
     *
     * <p>Implementation: linear scan over the bound contexts
     * collection. Acceptable because this is called only on entity
     * release (not on the per-event hot path). The set is unmodifiable
     * — callers MUST copy it if they intend to mutate.
     *
     * @param sbbId the SBB entity id (may be {@code null}, in which
     *              case the result is always empty)
     * @return the unmodifiable set of ACI names this SBB is attached
     *         to, or an empty set if the SBB is not registered
     */
    public Set<String> getAttachedAciNames(String sbbId) {
        if (sbbId == null) {
            return Collections.emptySet();
        }
        SimpleSbbLocalObject local = sbbs.get(sbbId);
        if (local == null) {
            return Collections.emptySet();
        }
        Set<String> result = new LinkedHashSet<>();
        for (ActivityContextInterface aci : activityContextNamingFacility.getBoundContexts()) {
            if (aci instanceof InMemoryActivityContext) {
                InMemoryActivityContext imac = (InMemoryActivityContext) aci;
                if (imac.getAttachedSbbs().contains(local)) {
                    String name = aci.getActivityContextName();
                    if (name != null) {
                        result.add(name);
                    }
                }
            }
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Sprint S9.2 — re-attach an SBB entity (identified by its
     * {@link SimpleSbbLocalObject}) to every ACI named in the supplied
     * set. Used by the cluster rehydration path to bring a freshly
     * reconstructed entity back to the same set of attachments it had
     * before failover.
     *
     * <p>Each re-attach goes through {@link #attach(String, SbbLocalObject)}
     * so the timer/tx side-effects are identical to a normal local
     * attach. ACI names that are not currently bound are silently
     * skipped with a WARN — they may be owned by a node that has
     * since been removed from the cluster view.
     *
     * @param sbbLocalObject the entity to re-attach (must be non-null)
     * @param aciNames       the set of ACI names to re-attach to
     *                       (must be non-null; may be empty)
     * @return the number of ACIs the entity was successfully re-attached to
     */
    public int reattachToAcis(SbbLocalObject sbbLocalObject, Set<String> aciNames) {
        Objects.requireNonNull(sbbLocalObject, "sbbLocalObject");
        Objects.requireNonNull(aciNames, "aciNames");
        int attached = 0;
        for (String name : aciNames) {
            if (name == null) {
                continue;
            }
            ActivityContextInterface aci = activityContextNamingFacility.lookup(name);
            if (aci == null) {
                LOG.warn("reattachToAcis: ACI '{}' is not bound on this node — skipping", name);
                continue;
            }
            try {
                attach(name, sbbLocalObject);
                attached++;
            } catch (RuntimeException ex) {
                LOG.warn("reattachToAcis: failed to re-attach '{}': {}", name, ex.getMessage());
            }
        }
        return attached;
    }

    /**
     * §11 — access the embedded {@link SimpleAlarmFacility}. Embedders
     * that want alarms to surface to operators retrieve the facility
     * from the container; the in-memory implementation tracks state and
     * logs through the shared {@link SimpleAlarmPort}.
     */
    public SimpleAlarmFacility getAlarmFacility() {
        return alarmFacility;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public MicroSleeConfiguration getConfiguration() {
        return configuration;
    }

    public VirtualThreadSbbEntityPool getSbbEntityPool() {
        return sbbEntityPool;
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 4 — RaCommandPort registry (injected into SBBs via @InjectRa)
    // ──────────────────────────────────────────────────────────

    /**
     * Register a {@link RaCommandPort} for the named RA entity.
     * Called during RA registration so the entity pool can later
     * inject the port into SBB fields annotated with {@code @InjectRa}.
     */
    public void registerRaCommandPort(String name, RaCommandPort port) {
        raCommandPorts.put(name, port);
        LOG.info("Registered RaCommandPort for RA [{}]", name);
    }

    /**
     * Look up the {@link RaCommandPort} registered for the given RA
     * entity name. Returns {@code null} when no port is registered.
     */
    public RaCommandPort getRaCommandPort(String name) {
        return raCommandPorts.get(name);
    }

    /**
     * Return the first available {@link RaCommandPort} when no
     * explicit RA name is specified in the {@code @InjectRa} annotation.
     * Returns {@code null} when no ports have been registered.
     */
    public RaCommandPort getDefaultRaCommandPort() {
        return raCommandPorts.values().stream().findFirst().orElse(null);
    }

    /**
     * Production P2.1 — access the bound {@code ClusterManager} instance,
     * or {@code null} when no cluster layer has been bound. The return
     * type is {@link Object} on purpose &mdash; the kernel does not depend
     * on {@code jainslee-cluster} at compile time, so embedders that need
     * the strongly-typed reference must cast it themselves (or use the
     * reflective helpers inside {@code ClusterManager}).
     */
    public Object getClusterManager() {
        return clusterManager;
    }

    /**
     * Phase A — access the CMP field store. Returns the in-memory backend
     * by default; embedders may wire in a Redis/JPA-backed implementation.
     */
    public CmpFieldStore getCmpFieldStore() {
        return cmpFieldStore;
    }

    /**
     * Phase 2 — access the profile facility. Returns the in-memory backend
     * by default; embedders may wire in a JPA / Redis-backed
     * implementation through {@link #installProfileFacility(ProfileFacility)}
     * before {@link #start()} runs.
     */
    public ProfileFacility getProfileFacility() {
        return profileFacility;
    }

    /**
     * Phase A — access the SBB lifecycle state machine used during
     * {@link #registerSbb(String, Sbb)}.
     */
    public SbbLifecycleManager getSbbLifecycleManager() {
        return sbbLifecycleManager;
    }

    /**
     * Phase B — factory that creates a child SBB entity under the given
     * parent id. Delegates to {@link #registerSbb(String, Sbb)} using a
     * freshly-constructed child SBB of the same class as the parent (this
     * is the simplest reasonable behaviour for an R&D container; a
     * production deployment would tie this to a service-bound
     * {@code <sbb-ref>} from the deployment descriptor).
     *
     * <p>Returns the local object of the new child so the parent SBB can
     * immediately narrow it to its typed interface.
     */
    public SbbLocalObject createChildSbb(String parentSbbId, String childSbbId) {
        return createChildSbb(parentSbbId, childSbbId, null);
    }

    /**
     * Phase B — create a child SBB entity under the given parent, using
     * {@code childFactory} to materialise the child instance. When
     * {@code childFactory} is {@code null}, the container falls back to
     * reflection (looking up a no-arg constructor on the parent's SBB
     * class) — which works for top-level classes but fails for anonymous
     * or non-static inner SBBs. Embedders that use anonymous classes
     * should pass an explicit factory.
     */
    public SbbLocalObject createChildSbb(String parentSbbId, String childSbbId,
                                         java.util.function.Function<String, Sbb> childFactory) {
        if (parentSbbId == null || childSbbId == null) {
            throw new IllegalArgumentException("parentSbbId and childSbbId are required");
        }
        SimpleSbbLocalObject parent = sbbs.get(parentSbbId);
        if (parent == null) {
            throw new IllegalStateException("Unknown parent SBB entity: " + parentSbbId);
        }
        Sbb childInstance;
        if (childFactory != null) {
            childInstance = childFactory.apply(childSbbId);
            if (childInstance == null) {
                throw new IllegalStateException(
                        "Child factory returned null for child " + childSbbId);
            }
        } else {
            // Re-use the parent's Sbb class for the child via reflection.
            // Production stacks derive this from the deployment descriptor;
            // micro-jainslee keeps the convention simple. We use the
            // declared no-arg constructor (rather than Class.newInstance
            // which is deprecated since Java 9 and also requires public
            // access) so private and package-private constructors are
            // accepted too.
            try {
                java.lang.reflect.Constructor<? extends Sbb> ctor =
                        parent.getSbb().getClass().getDeclaredConstructor();
                ctor.setAccessible(true);
                childInstance = ctor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot instantiate child SBB of class "
                        + parent.getSbb().getClass().getName() + ": " + e.getMessage(), e);
            }
        }
        return registerSbb(childSbbId, childInstance,
                new ServiceID(childSbbId, "com.microjainslee", "1.0"));
    }

    /**
     * Phase B — convenience: get the {@link ChildRelationFactory} that
     * child relations should use to spawn new children. The factory
     * simply delegates to {@link #createChildSbb(String, String)} with
     * a UUID-derived child id.
     */
    public ChildRelationFactory getChildRelationFactory() {
        return new ChildRelationFactory() {
            @Override
            public SbbLocalObject createChild(String parentSbbId) {
                String childId = parentSbbId + ".child." + java.util.UUID.randomUUID();
                return createChildSbb(parentSbbId, childId, null);
            }
        };
    }

    /**
     * Phase B — alternate factory that uses the given supplier to
     * materialise each child SBB instance. Use this when your SBB class
     * cannot be instantiated via reflection (anonymous / inner class).
     */
    public ChildRelationFactory getChildRelationFactory(
            final java.util.function.Function<String, Sbb> childSupplier) {
        return new ChildRelationFactory() {
            @Override
            public SbbLocalObject createChild(String parentSbbId) {
                String childId = parentSbbId + ".child." + java.util.UUID.randomUUID();
                return createChildSbb(parentSbbId, childId, childSupplier);
            }
        };
    }

    /**
     * Child-relation factory that acquires pooled entities for the given type
     * when registered, otherwise falls back to legacy {@link #registerSbb}.
     */
    public ChildRelationFactory getChildRelationFactory(final Class<? extends Sbb> childType) {
        if (childType == null) {
            throw new IllegalArgumentException("childType is required");
        }
        return new ChildRelationFactory() {
            @Override
            public SbbLocalObject createChild(String parentSbbId) {
                String childId = parentSbbId + ".child." + java.util.UUID.randomUUID();
                if (sbbTypeRegistry.isRegistered(childType)) {
                    return acquireEntity(childId, childType);
                }
                return createChildSbb(parentSbbId, childId, null);
            }
        };
    }

    private SimpleSbbLocalObject buildLocalObject(
            final String id,
            final SbbID sbbID,
            final Sbb sbb,
            final VirtualThreadSbbEntityPool.SbbEntity entity,
            final Class<? extends Sbb> pooledType,
            final boolean pooled,
            final EventMask effectiveMask,
            final ServiceID serviceID) {
        final SimpleSbbLocalObject localObject = new SimpleSbbLocalObject(
                sbbID,
                sbb,
                sbbEntityPool,
                new SimpleSbbLocalObject.RemovalListener() {
                    @Override
                    public void onRemoved(SimpleSbbLocalObject removedObject) {
                        detachFromAllActivityContexts(removedObject);
                        sbbs.remove(id);
                        entityTypesById.remove(id);
                        Sbb sbbInstance = removedObject.getSbb();
                        if (pooled && pooledType != null) {
                            SbbTypePool typePool = sbbTypeRegistry.find(pooledType);
                            if (typePool != null) {
                                if (sbbInstance instanceof CmpBackedSbb) {
                                    try {
                                        ((CmpBackedSbb) sbbInstance).cmpPersist();
                                    } catch (RuntimeException ignored) {
                                        // best effort
                                    }
                                }
                                sbbLifecycleManager.passivate(sbbInstance,
                                        removedObject.getEntityState().getCmpFields());
                                if (sbbInstance instanceof PoolableSbb) {
                                    try {
                                        ((PoolableSbb) sbbInstance).resetForReuse(id);
                                    } catch (RuntimeException ignored) {
                                        // best effort
                                    }
                                }
                                typePool.release(sbbInstance);
                            }
                        } else if (sbbInstance instanceof CmpBackedSbb) {
                            try {
                                ((CmpBackedSbb) sbbInstance).cmpPersist();
                            } catch (RuntimeException ignored) {
                                // best effort
                            }
                        }
                        // Sprint S6 — capture convergence key BEFORE the pool
                        // release clears the entity slot. Subscribers may
                        // still read CMP state through removedObject.
                        String convergenceKey = captureConvergenceKeyFor(id);
                        // Publish removal event (synchronous, fast subscribers only).
                        RemovalReason reason = resolveRemovalReason(removedObject);
                        entityRemovalBus.publish(new EntityRemovalEvent(
                                id, convergenceKey, reason, System.currentTimeMillis()));
                        entityRemovalCounter.incrementAndGet();
                        // Release pool AFTER bus so subscribers can still read CMP.
                        sbbEntityPool.release(entity);
                        cmpFieldStore.remove(id);
                    }
                },
                0,
                pooled);
        localObject.getEntityState().setEventMask(effectiveMask);
        if (sbb instanceof CmpBackedSbb) {
            CmpBackedSbb backed = (CmpBackedSbb) sbb;
            backed.setSbbEntityId(id);
            backed.setCmpFieldStore(cmpFieldStore);
        }
        return localObject;
    }

    private void activateEntity(final String id, final ServiceID serviceID, final SbbID sbbID,
            final SimpleSbbLocalObject localObject,
            final VirtualThreadSbbEntityPool.SbbEntity entity,
            final Sbb sbbInstance, final boolean pooledReuse) {
        entity.submit(new Runnable() {
            @Override
            public void run() {
                SimpleSbbContext ctx = new SimpleSbbContext(serviceID, localObject, sbbID, timerPort,
                        activityContextNamingFacility, profileFacility, alarmFacility);
                try {
                    if (pooledReuse) {
                        sbbInstance.setSbbContext(ctx);
                        Map<String, Object> cmpState = cmpFieldStore.load(id);
                        localObject.getEntityState().getCmpFields().putAll(cmpState);
                        sbbLifecycleManager.activate(sbbInstance, cmpState);
                    } else {
                        sbbLifecycleManager.create(sbbInstance, ctx, null);
                        sbbLifecycleManager.postCreate(sbbInstance);
                        Map<String, Object> cmpState = cmpFieldStore.load(id);
                        localObject.getEntityState().getCmpFields().putAll(cmpState);
                        sbbLifecycleManager.activate(sbbInstance, cmpState);
                    }
                    localObject.getEntityState().transitionTo(SbbLifecycleManager.State.READY);
                } catch (CreateException ce) {
                    LOG.warn("SBB {} failed sbbCreate/sbbPostCreate: {}", id, ce.getMessage());
                    sbbLifecycleManager.removeEntity(sbbInstance);
                    sbbs.remove(id);
                    entityTypesById.remove(id);
                    sbbEntityPool.release(entity);
                    cmpFieldStore.remove(id);
                    throw new RuntimeException(ce);
                } catch (RuntimeException re) {
                    LOG.error("SBB {} activation failed: {}", id, re.getMessage(), re);
                    sbbs.remove(id);
                    entityTypesById.remove(id);
                    cmpFieldStore.remove(id);
                    throw re;
                }
            }
        });
    }

    // -----------------------------------------------------------------
    // Production P2.2 — Activity Context Naming Facility abstraction
    // -----------------------------------------------------------------

    /**
     * Internal seam for the Activity Context Naming Facility.
     * jainslee-core keeps the {@code InMemoryActivityContextNamingFacility}
     * R&amp;D default; the cluster module's
     * {@code com.microjainslee.cluster.ClusteredActivityContextNamingFacility}
     * installs itself through
     * {@link MicroSleeContainer#bindActivityContextNamingFacility(Object)}
     * and is reached by the kernel through this interface.
     *
     * <p>The interface is package-private on purpose: it is an
     * implementation detail of the kernel/cluster hand-off, not a
     * public API surface.
     */
    public interface AcnfBackend extends ActivityContextNamingFacility {
        void bind(String name, ActivityContextInterface aci);
        ActivityContextInterface lookup(String name);
        void unbind(String name);
        java.util.Set<String> names();
        void clear();
        java.util.Collection<ActivityContextInterface> getBoundContexts();
    }

    /**
     * Adapter that wraps the in-memory facility so the kernel field
     * can hold a {@link AcnfBackend} without forcing the
     * {@code InMemoryActivityContextNamingFacility} class to grow a
     * new {@code implements} clause.
     */
    private static final class InMemoryAcnfBackend implements AcnfBackend {
        private final InMemoryActivityContextNamingFacility delegate;

        InMemoryAcnfBackend(InMemoryActivityContextNamingFacility delegate) {
            this.delegate = delegate;
        }

        @Override
        public void bind(String name, ActivityContextInterface aci) {
            delegate.bind(name, aci);
        }

        @Override
        public ActivityContextInterface lookup(String name) {
            return delegate.lookup(name);
        }

        @Override
        public void unbind(String name) {
            delegate.unbind(name);
        }

        @Override
        public java.util.Set<String> names() {
            // The in-memory facility does not expose a public
            // names() view (only lookup/bind/unbind/getBoundContexts);
            // build it from the bound contexts collection.
            java.util.Set<String> names = new java.util.LinkedHashSet<String>();
            for (ActivityContextInterface aci : delegate.getBoundContexts()) {
                if (aci != null) {
                    String name = aci.getActivityContextName();
                    if (name != null) {
                        names.add(name);
                    }
                }
            }
            return java.util.Collections.unmodifiableSet(names);
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public java.util.Collection<ActivityContextInterface> getBoundContexts() {
            return delegate.getBoundContexts();
        }
    }

    /**
     * Reflective adapter that lets a non-related
     * {@code com.microjainslee.cluster.ClusteredActivityContextNamingFacility}
     * sit in the {@link AcnfBackend} slot without forcing a
     * compile-time dependency on {@code jainslee-cluster}.
     *
     * <p>The wrapper forwards each call to the underlying cluster
     * facility through {@link java.lang.reflect.Method} handles
     * resolved once at construction time. Each call costs one
     * {@code Method.invoke} — acceptable because ACNF calls are not
     * on the per-event hot path.
     */
    private static final class ReflectiveAcnfBackend implements AcnfBackend {
        private final Object delegate;
        private final java.lang.reflect.Method bindMethod;
        private final java.lang.reflect.Method lookupMethod;
        private final java.lang.reflect.Method unbindMethod;
        private final java.lang.reflect.Method namesMethod;
        private final java.lang.reflect.Method clearMethod;
        private final java.lang.reflect.Method getBoundContextsMethod;

        ReflectiveAcnfBackend(Object delegate, Class<?> delegateClass) {
            this.delegate = delegate;
            this.bindMethod = lookupMethod0(delegateClass, "bind", String.class, ActivityContextInterface.class);
            this.lookupMethod = lookupMethod0(delegateClass, "lookup", String.class);
            this.unbindMethod = lookupMethod0(delegateClass, "unbind", String.class);
            this.namesMethod = lookupMethod0(delegateClass, "names");
            this.clearMethod = lookupMethod0(delegateClass, "clear");
            this.getBoundContextsMethod = lookupMethod0(delegateClass, "getBoundContexts");
        }

        private static java.lang.reflect.Method lookupMethod0(Class<?> cls, String name, Class<?>... params) {
            try {
                return cls.getMethod(name, params);
            } catch (NoSuchMethodException nsme) {
                throw new IllegalStateException(
                        "ClusteredActivityContextNamingFacility is missing method " + name
                                + " — the cluster module version is incompatible with this kernel.",
                        nsme);
            }
        }

        @Override
        public void bind(String name, ActivityContextInterface aci) {
            invoke(bindMethod, name, aci);
        }

        @Override
        public ActivityContextInterface lookup(String name) {
            return (ActivityContextInterface) invoke(lookupMethod, name);
        }

        @Override
        public void unbind(String name) {
            invoke(unbindMethod, name);
        }

        @Override
        public java.util.Set<String> names() {
            @SuppressWarnings("unchecked")
            java.util.Set<String> result = (java.util.Set<String>) invoke(namesMethod);
            return result != null ? result : java.util.Collections.emptySet();
        }

        @Override
        public void clear() {
            try {
                invoke(clearMethod);
            } catch (RuntimeException re) {
                // Clear is best-effort on the cluster path too.
            }
        }

        @Override
        public java.util.Collection<ActivityContextInterface> getBoundContexts() {
            @SuppressWarnings("unchecked")
            java.util.Collection<ActivityContextInterface> result =
                    (java.util.Collection<ActivityContextInterface>) invoke(getBoundContextsMethod);
            return result != null ? result : java.util.Collections.emptyList();
        }

        private Object invoke(java.lang.reflect.Method method, Object... args) {
            try {
                return method.invoke(delegate, args);
            } catch (java.lang.reflect.InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                if (cause instanceof Error) {
                    throw (Error) cause;
                }
                throw new RuntimeException(cause != null ? cause : ite);
            } catch (java.lang.IllegalAccessException iae) {
                throw new IllegalStateException(iae);
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 2 — Bootstrap port adapter for local RA endpoints
    // ──────────────────────────────────────────────────────────

    /**
     * Adapts the existing {@link SleeEndpointPortImpl} to the
     * {@link RaBootstrapPort} contract required by local RAs.
     *
     * <p>Delegates:
     * <ul>
     *   <li>{@code createActivityHandle} → creates a dual-implementing
     *       {@link RaActivityHandle} and starts the activity via
     *       {@link SleeEndpointPortImpl#startActivity}.</li>
     *   <li>{@code fireEvent} → {@link SleeEndpointPortImpl#fireEvent}
     *       bridging from {@link ActivityHandle} to
     *       {@link ActivityContextHandle}.</li>
     * </ul>
     */
    private static final class BootstrapPortAdapter implements RaBootstrapPort {
        private final SleeEndpointPortImpl endpoint;

        BootstrapPortAdapter(SleeEndpointPortImpl endpoint) {
            if (endpoint == null) {
                throw new IllegalArgumentException("endpoint is required");
            }
            this.endpoint = endpoint;
        }

        @Override
        public ActivityHandle createActivityHandle(String id) {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("handle id is required");
            }
            RaActivityHandle handle = new RaActivityHandle(id);
            endpoint.startActivity(handle, null);
            return handle;
        }

        @Override
        public void fireEvent(SleeEvent event, ActivityHandle handle, Address address) {
            if (event == null || handle == null) {
                return;
            }
            ActivityContextHandle ach = (handle instanceof ActivityContextHandle)
                    ? (ActivityContextHandle) handle
                    : new SimpleActivityContextHandle(handle.getId());
            endpoint.fireEvent(ach, event);
        }

        @Override
        public void endActivity(ActivityHandle handle) {
            if (handle == null) {
                return;
            }
            ActivityContextHandle ach = (handle instanceof ActivityContextHandle)
                    ? (ActivityContextHandle) handle
                    : new SimpleActivityContextHandle(handle.getId());
            endpoint.endActivity(ach);
        }
    }

    /**
     * Dual-purpose handle that satisfies both {@link ActivityHandle} (used by
     * {@link RaBootstrapPort}) and {@link ActivityContextHandle} (used by
     * {@link SleeEndpointPortImpl}). Implemented as a simple value object
     * keyed on the opaque id string.
     */
    private static final class RaActivityHandle implements ActivityHandle, ActivityContextHandle {
        private final String id;

        RaActivityHandle(String id) {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("handle id is required");
            }
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof RaActivityHandle)) return false;
            return id.equals(((RaActivityHandle) obj).id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return "RaActivityHandle[" + id + "]";
        }
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 4 — Simple RaCommandPort for SBB @InjectRa injection
    // ──────────────────────────────────────────────────────────

    /**
     * Minimal in-process {@link RaCommandPort} used during RA registration.
     * Commands are stored in a thread-safe queue for the RA to poll/consume.
     * Production embedders can replace this with a direct endpoint-backed
     * port (e.g. one that serializes commands over the wire).
     */
    static final class SimpleRaCommandPort implements RaCommandPort {
        private final String raEntityName;
        private final java.util.Queue<OutboundCommand> queue =
                new java.util.concurrent.ConcurrentLinkedQueue<>();

        SimpleRaCommandPort(String raEntityName) {
            this.raEntityName = raEntityName;
        }

        @Override
        public void sendCommand(OutboundCommand command) {
            queue.offer(command);
        }

        /** Drain all pending commands (called by the RA poll loop). */
        java.util.Queue<OutboundCommand> queue() {
            return queue;
        }

        String getRaEntityName() {
            return raEntityName;
        }
    }
}
