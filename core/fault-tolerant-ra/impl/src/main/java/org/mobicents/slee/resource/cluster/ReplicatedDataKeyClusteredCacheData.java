/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import org.jboss.cache.Fqn;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.cluster.cache.ClusteredCacheData;

/**
 * @author martins
 * 
 */
public class ReplicatedDataKeyClusteredCacheData<K extends Serializable, V extends Serializable>
		extends ClusteredCacheData {

	private final static Boolean DATA_MAP_KEY = Boolean.TRUE;

	/**
	 * @param nodeFqn
	 * @param mobicentsCluster
	 */
	@SuppressWarnings("unchecked")
	public ReplicatedDataKeyClusteredCacheData(
			ReplicatedDataCacheData parent, K key,
			MobicentsCluster mobicentsCluster) {
		super(Fqn.fromRelativeElements(parent.getNodeFqn(), key),
				mobicentsCluster);
	}

	@SuppressWarnings("unchecked")
	public V getValue() {
		return (V) getNode().get(DATA_MAP_KEY);
	}

	@SuppressWarnings("unchecked")
	public void setValue(V value) {
		getNode().put(DATA_MAP_KEY, value);
	}

}
