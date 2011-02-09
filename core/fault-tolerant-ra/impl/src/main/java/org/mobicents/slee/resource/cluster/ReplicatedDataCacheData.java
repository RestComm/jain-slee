/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.Set;

import org.jboss.cache.Fqn;
import org.mobicents.cache.CacheData;
import org.mobicents.cluster.MobicentsCluster;

/**
 * @author martins
 * 
 */
public class ReplicatedDataCacheData<K extends Serializable, V extends Serializable>
		extends CacheData {

	/**
	 * 
	 * @param name
	 * @param raEntity
	 * @param mobicentsCluster
	 */
	public ReplicatedDataCacheData(String rootName, String raEntity,
			MobicentsCluster mobicentsCluster) {
		super(Fqn.fromElements(rootName, raEntity), mobicentsCluster
				.getMobicentsCache());
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<K> getAllKeys() {
		return getNode().getChildrenNames();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key) {
		return getNode().hasChild(key);
	}
}
