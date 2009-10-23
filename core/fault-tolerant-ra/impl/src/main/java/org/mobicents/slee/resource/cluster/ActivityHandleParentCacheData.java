/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.Set;

import javax.slee.resource.ActivityHandle;

import org.jboss.cache.Fqn;
import org.mobicents.cache.CacheData;
import org.mobicents.cluster.MobicentsCluster;

/**
 * @author martins
 * 
 */
public class ActivityHandleParentCacheData<K extends Serializable & ActivityHandle, V extends Serializable>
		extends CacheData {

	/**
	 * 
	 * @param name
	 * @param raEntity
	 * @param mobicentsCluster
	 */
	public ActivityHandleParentCacheData(String rootName, String raEntity,
			MobicentsCluster mobicentsCluster) {
		super(Fqn.fromElements(rootName, raEntity), mobicentsCluster
				.getMobicentsCache());
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<K> getAllHandles() {
		return getNode().getChildrenNames();
	}

	/**
	 * 
	 * @param handle
	 * @return
	 */
	public boolean containsHandle(K handle) {
		return getNode().hasChild(handle);
	}
}
