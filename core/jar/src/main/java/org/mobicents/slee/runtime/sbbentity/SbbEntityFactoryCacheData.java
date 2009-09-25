package org.mobicents.slee.runtime.sbbentity;

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

public class SbbEntityFactoryCacheData extends CacheData {

	/**
	 * the fqn of the node that holds all activity context cache child nodes
	 */
	private final static Fqn parentNodeFqn = Fqn.fromElements(SbbEntityCacheData.parentNodeFqn);

	/**
	 * 
	 * @param cluster
	 */
	public SbbEntityFactoryCacheData(MobicentsCluster cluster) {
		super(parentNodeFqn, cluster.getMobicentsCache());
	}

	/**
	 * Retrieves a set containing sbb entity ids in the factory
	 * cache data
	 * 
	 * @return
	 */
	public Set<String> getSbbEntities() {
		final Node node = getNode();
		return node != null ? node.getChildrenNames() : Collections.EMPTY_SET;
	}

}