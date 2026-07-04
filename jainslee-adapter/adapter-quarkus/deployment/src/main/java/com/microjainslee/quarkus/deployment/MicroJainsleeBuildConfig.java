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

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import java.util.Optional;

/**
 * Build-time configuration for the micro-jainslee Quarkus extension.
 *
 * <p>Resolved at build time by the Quarkus deployment module and consumed by
 * {@link MicroJainsleeProcessor} when scheduling the recorder and synthetic beans.</p>
 *
 * <p>All keys are under the {@code microjainslee.*} prefix, e.g. {@code microjainslee.buffer-size=2048}.</p>
 */
@ConfigMapping(prefix = "microjainslee")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface MicroJainsleeBuildConfig {

    /**
     * Power-of-two ring-buffer size for the {@code EventRouter}'s LMAX Disruptor.
     * Larger values improve throughput at the cost of memory and worst-case latency.
     */
    @WithName("buffer-size")
    @WithDefault("1024")
    int bufferSize();

    /**
     * Whether the EventRouter should prefer Java virtual threads over a cached thread pool.
     */
    @WithName("prefer-virtual-threads")
    @WithDefault("true")
    boolean preferVirtualThreads();

    /**
     * Minimum number of SBB entity-pool entries kept warm.
     */
    @WithName("sbb-pool-min")
    @WithDefault("16")
    int sbbPoolMin();

    /**
     * Maximum number of SBB entity-pool entries.
     */
    @WithName("sbb-pool-max")
    @WithDefault("1024")
    int sbbPoolMax();

    /**
     * Whether to allocate SBB entity-pool entries on a per-virtual-thread basis.
     */
    @WithName("sbb-per-virtual-thread")
    @WithDefault("true")
    boolean sbbPerVirtualThread();

    @WithName("sbb-type-pool-min-idle")
    @WithDefault("0")
    int sbbTypePoolMinIdle();

    @WithName("event-delivery")
    @WithDefault("sync")
    String eventDelivery();

    /**
     * Register discovered {@code @SbbAnnotation} types with the container pool at startup.
     */
    @WithName("deployment.register-sbb-types")
    @WithDefault("true")
    boolean registerSbbTypes();

    /**
     * Whether the deployment processor should scan for {@code @Sbb}-annotated classes and
     * register synthetic beans for them. Disable for very large code bases where scanning
     * is too expensive.
     */
    @WithName("deployment.scan.enabled")
    @WithDefault("true")
    boolean scanEnabled();

    /**
     * Optional comma-separated list of class-name patterns to include during scan. Patterns
     * are matched as substrings against the fully-qualified class name. Empty means "all".
     */
    @WithName("deployment.scan.includes")
    Optional<String> scanIncludes();

    /**
     * Optional comma-separated list of class-name patterns to exclude during scan. Patterns
     * are matched as substrings against the fully-qualified class name.
     */
    @WithName("deployment.scan.excludes")
    Optional<String> scanExcludes();

    /**
     * Comma-separated list of RA entity class names to register via the 3-port contract.
     * Each class must implement both {@link com.microjainslee.api.RaEndpointPort} and
     * {@link com.microjainslee.api.RaCommandPort} (a single class serving both roles).
     * <p>
     * Example: {@code microjainslee.ra-registrations=com.example.MyHttpRa}
     */
    @WithName("ra-registrations")
    @WithDefault("")
    Optional<String> raRegistrations();

    /**
     * Event-to-SBB convergent routing mappings. Each entry maps a fully-qualified
     * {@link com.microjainslee.api.SleeEvent} class name to an SBB entity name.
     * <p>
     * Format: {@code eventClassName1=sbbName1,eventClassName2=sbbName2}
     * <p>
     * Example: {@code microjainslee.event-to-sbb-mappings=com.example.UssdBeginEvent=UssdSessionSbb}
     */
    @WithName("event-to-sbb-mappings")
    @WithDefault("")
    Optional<String> eventToSbbMappings();
}