/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.quarkus;

import com.microjainslee.api.UsagePort;
import com.microjainslee.core.SimpleUsagePort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quarkus usage facility — delegates to Micrometer when {@code quarkus-micrometer}
 * is on the classpath, otherwise falls back to {@link SimpleUsagePort}.
 */
public final class UsageFacilityQuarkusAdapter implements UsagePort {

    private static final Logger LOG = LogManager.getLogger(UsageFacilityQuarkusAdapter.class);

    private final UsagePort delegate;
    private final Object meterRegistry;
    private final ConcurrentHashMap<String, Object> counters =
            new ConcurrentHashMap<String, Object>();

    public UsageFacilityQuarkusAdapter() {
        this(null);
    }

    public UsageFacilityQuarkusAdapter(Object meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.delegate = meterRegistry == null ? new SimpleUsagePort() : null;
        if (meterRegistry != null) {
            LOG.debug("UsageFacilityQuarkusAdapter using Micrometer MeterRegistry");
        } else {
            LOG.debug("UsageFacilityQuarkusAdapter using in-memory SimpleUsagePort fallback");
        }
    }

    @Override
    public void incrementCounter(String counterName) {
        if (counterName == null) {
            return;
        }
        if (meterRegistry == null) {
            delegate.incrementCounter(counterName);
            return;
        }
        try {
            Object counter = counters.get(counterName);
            if (counter == null) {
                Method counterMethod = meterRegistry.getClass().getMethod("counter", String.class);
                counter = counterMethod.invoke(meterRegistry, "microjainslee.usage." + counterName);
                counters.putIfAbsent(counterName, counter);
                counter = counters.get(counterName);
            }
            counter.getClass().getMethod("increment").invoke(counter);
        } catch (ReflectiveOperationException e) {
            LOG.warnf(e, "Micrometer counter failed for %s — falling back to in-memory", counterName);
            fallback().incrementCounter(counterName);
        }
    }

    @Override
    public void recordSample(String parameterName, long value) {
        if (parameterName == null) {
            return;
        }
        if (meterRegistry == null) {
            delegate.recordSample(parameterName, value);
            return;
        }
        try {
            Method summaryMethod = meterRegistry.getClass().getMethod("summary", String.class);
            Object summary = summaryMethod.invoke(meterRegistry, "microjainslee.sample." + parameterName);
            summary.getClass().getMethod("record", double.class).invoke(summary, (double) value);
        } catch (ReflectiveOperationException e) {
            LOG.warnf(e, "Micrometer summary failed for %s — falling back to in-memory", parameterName);
            fallback().recordSample(parameterName, value);
        }
    }

    private UsagePort fallback() {
        if (delegate != null) {
            return delegate;
        }
        return new SimpleUsagePort();
    }
}
