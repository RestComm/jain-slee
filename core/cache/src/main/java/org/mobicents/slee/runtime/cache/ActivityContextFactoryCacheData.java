package org.mobicents.slee.runtime.cache;

import java.util.HashSet;
import java.util.Set;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;

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
	private final static Fqn parentNodeFqn = ActivityContextCacheData.parentNodeFqn;

	/**
	 * 
	 * @param activityContextId
	 */
	protected ActivityContextFactoryCacheData(Cache jBossCache) {
		super(parentNodeFqn, jBossCache);
	}

	/**
	 * Retrieves a set containing all activity context handles in the factory's
	 * cache data
	 * 
	 * @return
	 */
	public Set getActivityContextHandles() {
		Set result = new HashSet();
		for (Object obj :  getNode().getChildrenNames()) {
			result.add(obj);
		}
		return result;
	}

}