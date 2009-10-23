/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.jboss.cache.Fqn;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.cluster.cache.ClusteredCacheData;

/**
 * @author martins
 * 
 */
public class ActivityHandleClusteredCacheData<K extends Serializable & ActivityHandle, V extends Serializable>
		extends ClusteredCacheData {

	private final static Boolean HANDLE_NODE_DATA_MAP_KEY = Boolean.TRUE;

	/**
	 * @param nodeFqn
	 * @param mobicentsCluster
	 */
	@SuppressWarnings("unchecked")
	public ActivityHandleClusteredCacheData(
			ActivityHandleParentCacheData parent, K handle,
			MobicentsCluster mobicentsCluster) {
		super(Fqn.fromRelativeElements(parent.getNodeFqn(), handle),
				mobicentsCluster);
	}

	@SuppressWarnings("unchecked")
	public V getActivity() {
		return (V) getNode().get(HANDLE_NODE_DATA_MAP_KEY);
	}

	@SuppressWarnings("unchecked")
	public void setActivity(V activity) {
		getNode().put(HANDLE_NODE_DATA_MAP_KEY, activity);
	}

}
