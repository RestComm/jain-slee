package org.mobicents.slee.runtime.cache;

import java.util.HashSet;
import java.util.Set;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * 
 * Proxy object for profile manager data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ProfileManagerCacheData extends CacheData {

	/**
	 * the name of the cache node that holds all profile manager data
	 */
	public static final String CACHE_NODE_NAME = "profile-table";

	/**
	 * 
	 * @param txManager
	 */
	protected ProfileManagerCacheData(Cache jBossCache) {
		super(Fqn.fromElements(CACHE_NODE_NAME), jBossCache);
	}

	/**
	 * Retrieves all profile table names in the profile manager's cache data
	 * 
	 * @return
	 */
	public Set<String> getProfileTables() {
		Set<String> result = new HashSet<String>();
		for (Object obj : getNode().getChildren()) {
			Node profileTableNode = (Node) obj;
			result.add(profileTableNode.getFqn().getLastElementAsString());
		}
		return result;
	}

}
