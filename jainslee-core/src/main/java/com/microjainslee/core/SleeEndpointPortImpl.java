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
 * JAIN-SLEE 1.1 §11 — {@code SleeEndpointPort} implementation for embedded RAs.
 * Publishes events to the Disruptor ring without blocking the caller thread.
 */
public final class SleeEndpointPortImpl implements com.microjainslee.api.SleeEndpointPort {

    private final MicroSleeContainer container;
    private final String entityName;

    public SleeEndpointPortImpl(MicroSleeContainer container, String entityName) {
        if (container == null) {
            throw new IllegalArgumentException("container is required");
        }
        if (entityName == null || entityName.trim().isEmpty()) {
            throw new IllegalArgumentException("entityName is required");
        }
        this.container = container;
        this.entityName = entityName;
    }

    @Override
    public com.microjainslee.api.ActivityContextInterface startActivity(
            com.microjainslee.api.ActivityContextHandle handle, Object activity) {
        if (handle == null) {
            throw new IllegalArgumentException("handle is required");
        }
        String aciName = handle.getId();
        return container.createActivityContext(aciName);
    }

    @Override
    public void endActivity(com.microjainslee.api.ActivityContextHandle handle) {
        if (handle == null) {
            return;
        }
        // JAIN-SLEE 1.1 §7.3.4 — attached SBBs must observe the activity
        // end. Fire ActivityEndedEvent through the normal routing path
        // BEFORE the name binding disappears; the dispatch pipeline holds
        // the ACI object reference, so the unbind below only removes the
        // name → context mapping.
        com.microjainslee.api.ActivityContextInterface aci =
                container.getActivityContextNamingFacility().lookup(handle.getId());
        if (aci != null) {
            com.microjainslee.api.ActivityHandle eventHandle =
                    (handle instanceof com.microjainslee.api.ActivityHandle)
                            ? (com.microjainslee.api.ActivityHandle) handle
                            : new StringActivityHandle(handle.getId());
            container.routeEvent(
                    new com.microjainslee.api.ActivityEndedEvent(eventHandle), aci);
        }
        container.getActivityContextNamingFacility().unbind(handle.getId());
    }

    /** Minimal value-object handle used when the caller's handle type only
     *  implements {@code ActivityContextHandle}. */
    private static final class StringActivityHandle
            implements com.microjainslee.api.ActivityHandle {
        private final String id;

        StringActivityHandle(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    @Override
    public void fireEvent(com.microjainslee.api.ActivityContextHandle handle,
                          com.microjainslee.api.SleeEvent event) {
        if (handle == null || event == null) {
            return;
        }
        com.microjainslee.api.ActivityContextInterface aci =
                container.getActivityContextNamingFacility().lookup(handle.getId());
        if (aci == null) {
            throw new IllegalStateException("Unknown activity handle: " + handle.getId());
        }
        container.routeEvent(event, aci);
    }

    public String getEntityName() {
        return entityName;
    }
}
