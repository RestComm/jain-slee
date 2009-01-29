package org.mobicents.slee.runtime.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextFactoryCacheData extends CacheData {

	/**
	 * the fqn of the node that holds all activity context cache child nodes
	 */
	private final static Fqn parentNodeFqn = Fqn.fromElements("ac-factory");

	private final static Fqn HANDLES_CHILD_NODE_FQN = Fqn.fromElements("handles");
	private final static Fqn AC_IDS_CHILD_NODE_FQN = Fqn.fromElements("ids");

	private static final Object NODE_MAP_KEY = new Object();

	/**
	 * 
	 * @param activityContextId
	 */
	protected ActivityContextFactoryCacheData(Cache jBossCache) {
		super(Fqn.fromElements(parentNodeFqn), jBossCache);
	}

	/**
	 * Adds the activity context's handle and id in the factiry cache data.
	 * 
	 * @param handle
	 * @param id
	 * @return
	 */
	public boolean addActivityContext(Object handle, String id) {
		// add bidirectional mapping
		Node node = getNode();
		Fqn handleFqn = Fqn.fromRelativeElements(HANDLES_CHILD_NODE_FQN, handle);
		if (!node.hasChild(handleFqn)) {
			node.addChild(handleFqn).put(NODE_MAP_KEY, id);
			node.addChild(Fqn.fromRelativeElements(AC_IDS_CHILD_NODE_FQN, id)).put(
					NODE_MAP_KEY, handle);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Removes the activity context's handle and id in the factiry cache data.
	 * 
	 * @param handle
	 * @param id
	 */
	public void removeActivityContext(Object handle, String id) {

		if (handle == null) {
			throw new NullPointerException("null handle for id " + id);
		}
		if (id == null) {
			throw new NullPointerException("null id for handle " + handle);
		}

		// remove bidirectional mapping
		Node node = getNode();
		node.removeChild(Fqn.fromRelativeElements(HANDLES_CHILD_NODE_FQN, handle));
		node.removeChild(Fqn.fromRelativeElements(AC_IDS_CHILD_NODE_FQN, id));
	}

	/**
	 * Retrieves the activity context handle mapped to the specified activity
	 * context id
	 * 
	 * @param id
	 * @return
	 */
	public Object getActivityContextHandle(String id) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(AC_IDS_CHILD_NODE_FQN, id));
		if (childNode != null) {
			return childNode.get(NODE_MAP_KEY);
		} else {
			return null;
		}
	}

	/**
	 * Retrieves the activity context id mapped to the specified activity
	 * context handle
	 * 
	 * @param handle
	 * @return
	 */
	public String getActivityContextId(Object handle) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(HANDLES_CHILD_NODE_FQN, handle));
		if (childNode != null) {
			return (String) childNode.get(NODE_MAP_KEY);
		} else {
			return null;
		}
	}

	/**
	 * Retrieves a set containing all activity context handles in the factory's
	 * cache data
	 * 
	 * @return
	 */
	public Set getActivityContextHandles() {
		Set result = null;
		Node handlesNode = getNode().getChild(HANDLES_CHILD_NODE_FQN);
		if (handlesNode != null) {
			result = new HashSet();
			for (Object obj : handlesNode.getChildren()) {
				Node node = (Node) obj;
				result.add(node.getFqn().getLastElement());
			}
		} else {
			result = Collections.EMPTY_SET;
		}
		return result;
	}

	/**
	 * Retrieves a set containing all activity context ids in the factory's
	 * cache data
	 * 
	 * @return
	 */
	public Set getActivityContextIds() {
		Set result = null;
		Node idsNode = getNode().getChild(AC_IDS_CHILD_NODE_FQN);
		if (idsNode != null) {
			result = new HashSet();
			for (Object obj : idsNode.getChildren()) {
				Node node = (Node) obj;
				result.add(node.getFqn().getLastElement());
			}
		} else {
			result = Collections.EMPTY_SET;
		}
		return result;
	}

}