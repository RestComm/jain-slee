package com.microjainslee.core.child;

import com.microjainslee.api.SbbLocalObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * ChildRelation&lt;T extends SbbLocalObject&gt; implementation.
 *
 * Usage trong abstract SBB class:
 *
 *   // Parent SBB abstract class:
 *   public abstract ChildRelation<AuthSbbLocalObject> getAuthChildRelation();
 *
 *   // Concrete SBB usage:
 *   AuthSbbLocalObject auth = getAuthChildRelation().create();
 *   auth.authenticate(token); // direct typed call — no cast
 *
 *   // Iterate:
 *   for (SbbLocalObject child : getAuthChildRelation()) { ... }
 *
 *   // Remove one child:
 *   getAuthChildRelation().remove((AuthSbbLocalObject) target);
 *
 * Module: jainslee-core
 * Generated accessor: ConcreteSbbGenerator injects ChildRelationImpl instances
 *   for abstract getXxxChildRelation() methods.
 */
public class ChildRelationImpl<T extends SbbLocalObject> implements ChildRelation<T> {

    private static final Logger LOG = LogManager.getLogger(ChildRelationImpl.class);

    private final String parentEntityId;
    private final Class<T> childSbbLocalObjectClass;
    private final CascadeRemover cascadeRemover;

    /** Factory to allocate a new child SBB entity and return its typed LocalObject. */
    private final Supplier<T> childEntityFactory;

    // entityId → LocalObject (live children)
    private final Map<String, T> liveChildren = new ConcurrentHashMap<>();

    public ChildRelationImpl(String parentEntityId,
                             Class<T> childSbbLocalObjectClass,
                             CascadeRemover cascadeRemover,
                             Supplier<T> childEntityFactory) {
        this.parentEntityId = parentEntityId;
        this.childSbbLocalObjectClass = childSbbLocalObjectClass;
        this.cascadeRemover = cascadeRemover;
        this.childEntityFactory = childEntityFactory;
    }

    /**
     * Create new child SBB entity.
     * Child được attached to parent — cascade remove khi parent dies.
     */
    @Override
    public T create() {
        T childLocalObject = childEntityFactory.get();
        String childEntityId = extractEntityId(childLocalObject);

        // Register parent-child relationship
        cascadeRemover.registerChild(parentEntityId, childEntityId);
        liveChildren.put(childEntityId, childLocalObject);

        LOG.debugf("Child created: parent=%s child=%s type=%s",
                parentEntityId, childEntityId, childSbbLocalObjectClass.getSimpleName());
        return childLocalObject;
    }

    /**
     * Remove a specific child entity.
     * Triggers cascade removal per spec §6.7 (depth-first from this child).
     */
    @Override
    public void remove(T child) {
        String childEntityId = extractEntityId(child);
        liveChildren.remove(childEntityId);
        cascadeRemover.deregisterChild(parentEntityId, childEntityId);
        cascadeRemover.cascadeRemove(childEntityId);
        LOG.debugf("Child removed explicitly: %s from parent %s", childEntityId, parentEntityId);
    }

    /** @return true if no live children. */
    @Override
    public boolean isEmpty() {
        return liveChildren.isEmpty();
    }

    /** @return number of live children. */
    @Override
    public int size() {
        return liveChildren.size();
    }

    /** Iterator over live child LocalObjects. */
    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableCollection(liveChildren.values()).iterator();
    }

    /** Get all children as unmodifiable list. */
    public List<T> getAll() {
        return List.copyOf(liveChildren.values());
    }

    // Internal: extract entity ID from LocalObject
    // LocalObject must implement HasEntityId or we use identity hash as fallback
    private String extractEntityId(T localObject) {
        if (localObject instanceof HasEntityId hei) {
            return hei.getEntityId();
        }
        // Fallback: use identity hash (not ideal, but prevents NPE)
        LOG.warnf("SbbLocalObject %s does not implement HasEntityId — using identity hash",
                localObject.getClass().getSimpleName());
        return "entity-" + System.identityHashCode(localObject);
    }

    /** Marker interface — generated LocalObject impls must implement this. */
    public interface HasEntityId {
        String getEntityId();
    }
}
