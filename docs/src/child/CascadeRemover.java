package com.microjainslee.core.child;

import com.microjainslee.api.Sbb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Spec §6.7 compliant cascade SBB removal.
 *
 * JAIN SLEE 1.1 §6.7:
 *   "When a root SBB entity is removed, all child SBB entities must also
 *    be removed. sbbRemove() is called on child SBB entities before parent."
 *
 * Order: depth-first post-order (leaf-first):
 *   parent
 *   ├── child-A
 *   │   └── grandchild-A1 → sbbRemove() FIRST
 *   │   └── grandchild-A2 → sbbRemove() SECOND
 *   │   child-A → sbbRemove() THIRD
 *   └── child-B → sbbRemove() FOURTH
 *   parent → sbbRemove() LAST (or never if pool handles it)
 *
 * Without this: orphaned child SBBs, resource leaks, TCK ChildSbbRelation group fails.
 *
 * Module: jainslee-core (update VirtualThreadSbbEntityPool to use this)
 */
public class CascadeRemover {

    private static final Logger LOG = LogManager.getLogger(CascadeRemover.class);

    /** Lookup SBB instance by entityId. */
    public interface EntityLookup {
        Sbb getSbb(String entityId);
        void releaseEntity(String entityId);
    }

    // parentId → Set<childId>
    private final ConcurrentHashMap<String, Set<String>> parentToChildren
            = new ConcurrentHashMap<>();

    // childId → parentId (reverse index)
    private final ConcurrentHashMap<String, String> childToParent
            = new ConcurrentHashMap<>();

    private final EntityLookup entityLookup;

    public CascadeRemover(EntityLookup entityLookup) {
        this.entityLookup = entityLookup;
    }

    /** Register parent-child relationship khi ChildRelation.create() được gọi. */
    public void registerChild(String parentId, String childId) {
        parentToChildren.computeIfAbsent(parentId, k -> ConcurrentHashMap.newKeySet())
                        .add(childId);
        childToParent.put(childId, parentId);
        LOG.tracef("Child registered: %s → %s", parentId, childId);
    }

    /** Deregister child (ví dụ: child removed explicitly qua ChildRelation.remove()). */
    public void deregisterChild(String parentId, String childId) {
        Set<String> children = parentToChildren.get(parentId);
        if (children != null) {
            children.remove(childId);
        }
        childToParent.remove(childId);
    }

    /**
     * Cascade remove — depth-first post-order.
     * Gọi từ SbbEntityPool khi root SBB entity bị removed.
     *
     * @param rootEntityId entity cần remove (có thể là root hoặc intermediate)
     * @param visited      Set để tránh vòng lặp (nên pass new HashSet<>() từ caller)
     */
    public void cascadeRemove(String rootEntityId, Set<String> visited) {
        if (!visited.add(rootEntityId)) {
            LOG.warnf("Cycle detected in SBB entity tree at %s — skipping", rootEntityId);
            return;
        }

        // 1. Depth-first: remove all children first
        Set<String> children = parentToChildren.remove(rootEntityId);
        if (children != null) {
            for (String childId : new HashSet<>(children)) {
                cascadeRemove(childId, visited);
            }
        }

        // 2. Call sbbRemove() on this entity's SBB instance
        Sbb sbb = entityLookup.getSbb(rootEntityId);
        if (sbb != null) {
            try {
                sbb.sbbRemove();
                LOG.tracef("sbbRemove() called on %s", rootEntityId);
            } catch (Throwable t) {
                // Spec: exceptions in sbbRemove() are logged, removal continues
                LOG.errorf(t, "sbbRemove() threw exception for entity %s — continuing removal",
                        rootEntityId);
            }
        }

        // 3. Clean up indexes
        String parentId = childToParent.remove(rootEntityId);
        if (parentId != null) {
            Set<String> siblingSet = parentToChildren.get(parentId);
            if (siblingSet != null) {
                siblingSet.remove(rootEntityId);
            }
        }

        // 4. Release entity back to pool
        entityLookup.releaseEntity(rootEntityId);
    }

    /** Convenience: cascade remove với fresh visited set. */
    public void cascadeRemove(String rootEntityId) {
        cascadeRemove(rootEntityId, new HashSet<>());
    }

    /** Get children set (immutable view). */
    public Set<String> getChildren(String parentId) {
        Set<String> children = parentToChildren.get(parentId);
        return children != null ? Set.copyOf(children) : Set.of();
    }

    /** Kiểm tra có phải là child của parent nào đó không. */
    public boolean isChild(String entityId) {
        return childToParent.containsKey(entityId);
    }

    /** Số entity trong tree — cho metrics. */
    public int totalTrackedEntities() {
        return parentToChildren.size() + childToParent.size();
    }
}
