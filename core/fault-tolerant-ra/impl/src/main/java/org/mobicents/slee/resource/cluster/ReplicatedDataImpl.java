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
public class ReplicatedDataImpl<K extends Serializable, V extends Serializable>
		implements ReplicatedData<K, V> {

	/**
	 * 
	 */
	private final MobicentsCluster cluster;

	/**
	 * 
	 */
	private final ReplicatedDataCacheData<K, V> cacheData;

	/**
	 * @param cacheData
	 * @param raEntity
	 */
	public ReplicatedDataImpl(String name, String raEntity,
			MobicentsCluster cluster) {
		cacheData = new ReplicatedDataCacheData<K, V>(name, raEntity,
				cluster);
		cacheData.create();
		this.cluster = cluster;
	}

	/**
	 * 
	 * @return the cacheData
	 */
	ReplicatedDataCacheData<K, V> getCacheData() {
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
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#getLocalKeyset()
	 */
	public Set<K> getLocalKeyset() {
		Set<K> set = new HashSet<K>();
		ReplicatedDataKeyClusteredCacheData<K, V> handleCacheData = null;
		Address handleCacheDataClusterNode = null;
		for (K handle : cacheData.getAllKeys()) {
			handleCacheData = new ReplicatedDataKeyClusteredCacheData<K, V>(
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
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#put(java.io.Serializable, java.io.Serializable)
	 */
	public boolean put(K key, V value) {
		final ReplicatedDataKeyClusteredCacheData<K, V> keyCacheData = new ReplicatedDataKeyClusteredCacheData<K, V>(
				cacheData, key, cluster);
		boolean created = keyCacheData.create();
		if (value != null) {
			keyCacheData.setValue(value);
		}
		return created;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#get(java.io.Serializable)
	 */
	public V get(K key) {
		final ReplicatedDataKeyClusteredCacheData<K, V> handleCacheData = new ReplicatedDataKeyClusteredCacheData<K, V>(
				cacheData, key, cluster);
		return handleCacheData.exists() ? handleCacheData.getValue() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#contains(java.io.Serializable)
	 */
	public boolean contains(K key) {
		return new ReplicatedDataKeyClusteredCacheData<K, V>(cacheData, key,
				cluster).exists();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#remove(java.io.Serializable)
	 */
	public boolean remove(K key) {
		return new ReplicatedDataKeyClusteredCacheData<K, V>(cacheData, key, cluster)
				.remove();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#getKeyset()
	 */
	public Set<K> getKeyset() {
		return cacheData.getAllKeys();
	}

}
