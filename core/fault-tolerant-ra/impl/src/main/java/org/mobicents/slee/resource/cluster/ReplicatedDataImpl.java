/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jgroups.Address;
import org.mobicents.cluster.MobicentsCluster;

/**
 * @author martins
 * 
 */
public class ReplicatedDataImpl<K extends SerializableActivityHandle, V extends Serializable>
		implements ReplicatedData<K, V> {

	/**
	 * 
	 */
	private final MobicentsCluster cluster;

	/**
	 * 
	 */
	private final ActivityHandleParentCacheData<K, V> cacheData;

	/**
	 * @param cacheData
	 * @param raEntity
	 */
	public ReplicatedDataImpl(String name, String raEntity,
			MobicentsCluster cluster) {
		cacheData = new ActivityHandleParentCacheData<K, V>(name, raEntity,
				cluster);
		cacheData.create();
		this.cluster = cluster;
	}

	/**
	 * 
	 * @return the cacheData
	 */
	ActivityHandleParentCacheData<K, V> getCacheData() {
		return cacheData;
	}

	/**
	 * 
	 * @return the cluster
	 */
	MobicentsCluster getCluster() {
		return cluster;
	}

	/**
	 * Removes all replicated data
	 */
	public void remove() {
		cacheData.remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.ReplicatedActivities#getLocalHandles
	 * ()
	 */
	public Set<K> getLocalHandles() {
		Set<K> set = new HashSet<K>();
		ActivityHandleClusteredCacheData<K, V> handleCacheData = null;
		Address handleCacheDataClusterNode = null;
		for (K handle : cacheData.getAllHandles()) {
			handleCacheData = new ActivityHandleClusteredCacheData<K, V>(
					cacheData, handle, cluster);
			handleCacheDataClusterNode = handleCacheData
					.getClusterNodeAddress();
			if (handleCacheDataClusterNode == null
					|| handleCacheDataClusterNode.equals(cluster
							.getLocalAddress())) {
				set.add(handle);
			}
		}
		return Collections.unmodifiableSet(set);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.ReplicatedActivities#put(org.mobicents
	 * .slee.resource.cluster.SerializableActivityHandle, java.io.Serializable)
	 */
	public boolean put(K handle, V activity) {
		final ActivityHandleClusteredCacheData<K, V> handleCacheData = new ActivityHandleClusteredCacheData<K, V>(
				cacheData, handle, cluster);
		boolean created = handleCacheData.create();
		if (activity != null) {
			handleCacheData.setActivity(activity);
		}
		return created;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.ReplicatedActivities#get(org.mobicents
	 * .slee.resource.cluster.SerializableActivityHandle)
	 */
	public V get(K handle) {
		final ActivityHandleClusteredCacheData<K, V> handleCacheData = new ActivityHandleClusteredCacheData<K, V>(
				cacheData, handle, cluster);
		return handleCacheData.exists() ? handleCacheData.getActivity() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.ReplicatedActivities#contains(org
	 * .mobicents.slee.resource.cluster.SerializableActivityHandle)
	 */
	public boolean contains(K handle) {
		return new ActivityHandleClusteredCacheData<K, V>(cacheData, handle,
				cluster).exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.ReplicatedActivities#remove(org.mobicents
	 * .slee.resource.cluster.SerializableActivityHandle)
	 */
	public void remove(K handle) {
		new ActivityHandleClusteredCacheData<K, V>(cacheData, handle, cluster)
				.remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.ReplicatedActivities#getReplicatedHandles
	 * ()
	 */
	public Set<K> getReplicatedHandles() {
		return cacheData.getAllHandles();
	}

}
