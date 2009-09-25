package org.mobicents.slee.runtime.activity;

import java.util.Collections;
import java.util.Set;

import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.mobicents.cache.CacheData;
import org.mobicents.cluster.MobicentsCluster;

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
	private final static Fqn parentNodeFqn = Fqn.fromElements(ActivityContextCacheData.parentNodeFqn);

	/**
	 * 
	 * @param activityContextId
	 */
	public ActivityContextFactoryCacheData(MobicentsCluster cluster) {
		super(parentNodeFqn, cluster.getMobicentsCache());
	}

	/**
	 * Retrieves a set containing all activity context handles in the factory's
	 * cache data
	 * 
	 * @return
	 */
	public Set getActivityContextHandles() {
		final Node node = getNode();
		return node != null ? node.getChildrenNames() : Collections.EMPTY_SET;
	}

}