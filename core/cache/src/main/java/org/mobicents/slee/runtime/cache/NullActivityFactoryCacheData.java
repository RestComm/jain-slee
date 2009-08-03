package org.mobicents.slee.runtime.cache;

import java.util.HashSet;
import java.util.Set;

import javax.slee.nullactivity.NullActivity;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;

/**
 * 
 * Proxy object for null ac factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class NullActivityFactoryCacheData extends CacheData {

	/**
	 * root fqn
	 */
	private final static Fqn parentNodeFqn = Fqn.ROOT;
	/**
	 * the name of the cache node that holds all data
	 */
	private static final String CACHE_NODE_NAME = "null-ac-factory";

	/**
	 * 
	 * @param txManager
	 */
	protected NullActivityFactoryCacheData(Cache jBossCache) {
		super(Fqn.fromRelativeElements(parentNodeFqn, CACHE_NODE_NAME),
				jBossCache);
	}

	/**
	 * Adds the id of a {@link NullActivity} in the factory's cache data.
	 * 
	 * @param id
	 */
	public void addNullActivityId(String id) {
		getNode().addChild(Fqn.fromElements(id));
	}

	/**
	 * Removes the id of a {@link NullActivity} in the factory's cache data.
	 * 
	 * @param id
	 * @return
	 */
	public boolean removeNullActivityId(String id) {
		return getNode().removeChild(Fqn.fromElements(id));
	}

	/**
	 * Retrieves all ids for {@link NullActivity}s in the factory cache data.
	 * 
	 * @return
	 */
	public Set<String> getNullActivityIds() {
		return new HashSet<String>(getNode().getChildrenNames());		
	}
}
