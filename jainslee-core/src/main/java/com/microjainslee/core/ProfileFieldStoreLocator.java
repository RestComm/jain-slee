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

/**
 * Locator for the profile CMP field store used by
 * {@link com.microjainslee.api.ProfileAccessorInvoker}.
 *
 * <p>Resolution order: the thread-local binding (tests running several
 * containers on separate threads), then the JVM-global binding. The global
 * fallback is essential — SBB event handlers run on event-router /
 * virtual-thread executors, never on the thread that constructed the
 * container, so a pure ThreadLocal would make every profile read from an
 * event handler fail with "No ProfileFieldStore registered".
 */
public final class ProfileFieldStoreLocator {

    private static final ThreadLocal<InMemoryProfileFacility> CURRENT =
            new ThreadLocal<InMemoryProfileFacility>();
    private static volatile InMemoryProfileFacility global;

    private ProfileFieldStoreLocator() {
        // utility
    }

    /** Bind the facility for the current thread AND as the JVM-global default. */
    public static void set(InMemoryProfileFacility facility) {
        if (facility == null) {
            InMemoryProfileFacility previous = CURRENT.get();
            CURRENT.remove();
            // Only clear the global slot when it points at the facility
            // being unbound on this thread — a second container's shutdown
            // must not yank the store from a still-running first container.
            if (previous != null && previous == global) {
                global = null;
            }
        } else {
            CURRENT.set(facility);
            global = facility;
        }
    }

    /** Thread-local binding first, JVM-global fallback second. */
    public static InMemoryProfileFacility get() {
        InMemoryProfileFacility local = CURRENT.get();
        return local != null ? local : global;
    }

    /** Explicitly clear the JVM-global binding (container shutdown). */
    static void clearGlobal(InMemoryProfileFacility facility) {
        if (facility == null || facility == global) {
            global = null;
        }
    }
}
