/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.quarkus.deployment;

import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.TimerPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.annotations.SbbAnnotation;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.quarkus.MicroJainsleeProducer;
import com.microjainslee.quarkus.MicroJainsleeRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.ShutdownContextBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Quarkus build-step processor for the micro-jainslee extension.
 */
public class MicroJainsleeProcessor {
    // jboss-logging only — Log4j2 on the deployment classpath clashes with the
    // Quarkus Maven plugin realm (NoSuchFieldError on DefaultFlowMessageFactory).
    private static final org.jboss.logging.Logger LOG =
            org.jboss.logging.Logger.getLogger(MicroJainsleeProcessor.class);

    private static final DotName SBB_ANNOTATION = DotName.createSimple(SbbAnnotation.class.getName());
    private static final String FEATURE_NAME = "micro-jainslee";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @BuildStep
    AdditionalBeanBuildItem runtimeBeans() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClasses(MicroJainsleeProducer.class.getName())
                .setUnremovable()
                .build();
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void startContainer(MicroJainsleeRecorder recorder) {
        recorder.startContainer();
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerDiscoveredSbbTypes(MicroJainsleeRecorder recorder,
                                    CombinedIndexBuildItem indexBuildItem,
                                    MicroJainsleeBuildConfig config) {
        if (!config.registerSbbTypes()) {
            return;
        }
        IndexView index = indexBuildItem.getIndex();
        java.util.List<String> types = new java.util.ArrayList<String>();
        for (org.jboss.jandex.AnnotationInstance ai : index.getAnnotations(SBB_ANNOTATION)) {
            if (ai.target() == null || !ai.target().kind().equals(org.jboss.jandex.AnnotationTarget.Kind.CLASS)) {
                continue;
            }
            org.jboss.jandex.ClassInfo ci = ai.target().asClass();
            if (ci == null || ci.name() == null) {
                continue;
            }
            String fqn = ci.name().toString();
            if (implementsSbb(ci)) {
                types.add(fqn);
            }
        }
        recorder.registerSbbTypes(types);
    }

    private static final org.jboss.jandex.DotName SBB_INTERFACE =
            org.jboss.jandex.DotName.createSimple(Sbb.class.getName());

    private static boolean implementsSbb(org.jboss.jandex.ClassInfo ci) {
        return ci.interfaceNames().contains(SBB_INTERFACE);
    }

    /**
     * Expose the container as a STATIC_INIT synthetic bean so normal {@code @Inject}
     * into application beans (e.g. bootstrap) resolves at build time.
     */
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    void containerAndFacilityBeans(MicroJainsleeRecorder recorder,
                                   MicroJainsleeBuildConfig buildConfig,
                                   BuildProducer<SyntheticBeanBuildItem> beans) {
        int bufferSize = powerOfTwo(buildConfig.bufferSize(), "microjainslee.container.buffer-size");
        LOG.debugf("Registering synthetic bean for MicroSleeContainer (bufferSize=%s, sbbPool=%s-%s, perVT=%s)",
                bufferSize,
                buildConfig.sbbPoolMin(), buildConfig.sbbPoolMax(),
                buildConfig.sbbPerVirtualThread());
        io.quarkus.runtime.RuntimeValue<MicroSleeContainer> container =
                recorder.createContainer(
                        bufferSize,
                        buildConfig.preferVirtualThreads(),
                        buildConfig.sbbPoolMin(),
                        buildConfig.sbbPoolMax(),
                        buildConfig.sbbPerVirtualThread(),
                        buildConfig.sbbTypePoolMinIdle(),
                        buildConfig.eventDelivery(),
                        buildConfig.offHeapEnabled(),
                        buildConfig.offHeapStorageDir().orElse(""));
        beans.produce(SyntheticBeanBuildItem.configure(MicroSleeContainer.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .runtimeValue(container)
                .done());
        beans.produce(SyntheticBeanBuildItem.configure(EventRouter.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .runtimeValue(recorder.eventRouterOf(container))
                .done());
        beans.produce(SyntheticBeanBuildItem.configure(TimerPort.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .runtimeValue(recorder.timerPortOf(container))
                .done());
        beans.produce(SyntheticBeanBuildItem.configure(MicroSleeContainer.AcnfBackend.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .runtimeValue(recorder.acnfOf(container))
                .done());
    }

    /**
     * Scan the Jandex index for {@code @SbbAnnotation}-annotated classes and register a
     * synthetic bean for each. Only runs when {@code microjainslee.container.deployment.scan.enabled=true}.
     */
    @BuildStep
    void sbbSyntheticBeans(BuildProducer<SyntheticBeanBuildItem> beans,
                           CombinedIndexBuildItem indexBuildItem,
                           MicroJainsleeBuildConfig config) {
        if (!config.scanEnabled()) {
            LOG.info("@Sbb scan disabled (microjainslee.container.deployment.scan.enabled=false)");
            return;
        }
        IndexView index = indexBuildItem.getIndex();
        Set<String> includes = splitCsv(config.scanIncludes());
        Set<String> excludes = splitCsv(config.scanExcludes());

        int registered = 0;
        for (org.jboss.jandex.AnnotationInstance ai : index.getAnnotations(SBB_ANNOTATION)) {
            if (ai.target() == null || !ai.target().kind().equals(org.jboss.jandex.AnnotationTarget.Kind.CLASS)) {
                continue;
            }
            org.jboss.jandex.ClassInfo ci = ai.target().asClass();
            if (ci == null || ci.name() == null) {
                continue;
            }
            String fqn = ci.name().toString();
            if (!matches(fqn, includes, excludes)) {
                LOG.debugf("Skipping @Sbb (filter mismatch): %s", fqn);
                continue;
            }
            if (ci.isAbstract() || ci.isInterface()) {
                LOG.debugf("Skipping @Sbb CDI synthetic bean for abstract/interface %s "
                        + "(register via MicroSleeContainer.registerSbbType)", fqn);
                continue;
            }
            Class<?> beanClass;
            try {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl == null) {
                    cl = MicroJainsleeProcessor.class.getClassLoader();
                }
                beanClass = Class.forName(fqn, false, cl);
            } catch (ClassNotFoundException e) {
                LOG.warnf("Failed to load @Sbb class %s on build TCCL — "
                        + "enable only for app types visible to augmentation, or keep "
                        + "microjainslee.container.deployment.scan.enabled=false", fqn);
                continue;
            }
            try {
                beanClass.getConstructor(); // public no-arg only
            } catch (NoSuchMethodException e) {
                LOG.debugf("Skipping @Sbb CDI synthetic bean for %s (no public no-arg ctor; "
                        + "use registerSbbType with a supplier)", fqn);
                continue;
            }
            beans.produce(SyntheticBeanBuildItem.configure(beanClass)
                    .scope(ApplicationScoped.class)
                    .unremovable()
                    .done());
            registered++;
            LOG.infof("Discovered @Sbb %s -> registering synthetic bean", fqn);
        }
        LOG.infof("@Sbb scan complete: %s bean(s) registered", registered);
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 2 — RA registration from build-time config
    // ──────────────────────────────────────────────────────────

    /**
     * Discover {@link com.microjainslee.api.RaEndpointPort}/{@link com.microjainslee.api.RaCommandPort}
     * implementations at build time and register them via the recorder.
     * <p>
     * Classes listed in {@code microjainslee.ra-registrations} must implement <b>both</b>
     * {@code RaEndpointPort} and {@code RaCommandPort}. They are instantiated via no-arg
     * constructor and registered with the container during {@code RUNTIME_INIT}.
     */
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerRasFromConfig(MicroJainsleeRecorder recorder, MicroJainsleeBuildConfig config) {
        if (!config.raRegistrations().isPresent() || config.raRegistrations().get().trim().isEmpty()) {
            LOG.debug("No ra-registrations configured; skipping build-time RA registration");
            return;
        }
        for (String fqn : splitCsv(config.raRegistrations())) {
            // Validate the class exists and implements both ports at build time,
            // but delegate actual instantiation to the recorder at RUNTIME_INIT.
            try {
                Class<?> clazz = Class.forName(fqn);
                if (!RaEndpointPort.class.isAssignableFrom(clazz)) {
                    LOG.warnf("Class %s does not implement RaEndpointPort — skipping", fqn);
                    continue;
                }
                if (!RaCommandPort.class.isAssignableFrom(clazz)) {
                    LOG.warnf("Class %s does not implement RaCommandPort — skipping", fqn);
                    continue;
                }
            } catch (ClassNotFoundException e) {
                LOG.warnf("RA class not found: %s — skipping", fqn);
                continue;
            }
            // Safe: passes only the class name string through to the recorder.
            recorder.registerRaFromClassName(fqn);
        }
    }

    // ──────────────────────────────────────────────────────────
    // GOAL 5 — event-to-SBB mappings from build-time config
    // ──────────────────────────────────────────────────────────

    /**
     * Parse {@code microjainslee.event-to-sbb-mappings} and register each
     * mapping via the recorder at {@code RUNTIME_INIT}.
     * <p>
     * Format: {@code com.example.EventA=sbbNameA,com.example.EventB=sbbNameB}
     */
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void mapEventsToSbbsFromConfig(MicroJainsleeRecorder recorder, MicroJainsleeBuildConfig config) {
        if (!config.eventToSbbMappings().isPresent() || config.eventToSbbMappings().get().trim().isEmpty()) {
            LOG.debug("No event-to-sbb-mappings configured; skipping");
            return;
        }
        for (String mapping : config.eventToSbbMappings().get().split(",")) {
            String trimmed = mapping.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            int eqIdx = trimmed.indexOf('=');
            if (eqIdx < 1 || eqIdx >= trimmed.length() - 1) {
                LOG.warnf("Invalid event-to-sbb mapping (expected eventClass=sbbName): %s", trimmed);
                continue;
            }
            String eventClass = trimmed.substring(0, eqIdx).trim();
            String sbbName = trimmed.substring(eqIdx + 1).trim();
            recorder.mapEventToSbb(eventClass, sbbName);
        }
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void shutdownContainer(MicroJainsleeRecorder recorder, ShutdownContextBuildItem shutdown) {
        LOG.debug("Wiring shutdown hook for MicroSleeContainer");
        // Quarkus injects ShutdownContextBuildItem here and passes the runtime
        // ShutdownContext into the recorder method.
        recorder.registerShutdownHook(shutdown);
    }

    // ----- helpers -----

    private static int powerOfTwo(int value, String propName) {
        if (value <= 0 || Integer.bitCount(value) != 1) {
            String msg = propName + " must be a positive power of two (was " + value + ")";
            LOG.error(msg);
            throw new IllegalArgumentException(msg);
        }
        return value;
    }

    private static Set<String> splitCsv(Optional<String> csv) {
        Set<String> out = new HashSet<String>();
        if (csv.isPresent()) {
            for (String s : csv.get().split(",")) {
                String t = s.trim();
                if (!t.isEmpty()) {
                    out.add(t);
                }
            }
        }
        return out;
    }

    private static boolean matches(String fqn, Set<String> includes, Set<String> excludes) {
        if (!includes.isEmpty()) {
            boolean ok = false;
            for (String pat : includes) {
                if (fqn.contains(pat)) { ok = true; break; }
            }
            if (!ok) return false;
        }
        for (String pat : excludes) {
            if (fqn.contains(pat)) return false;
        }
        return true;
    }
}
