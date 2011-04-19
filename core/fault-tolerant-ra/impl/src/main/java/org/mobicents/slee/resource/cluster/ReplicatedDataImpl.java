/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
	 * 
	 */
	private final DataRemovalListener<K, V> dataRemovalListener;
	
	/**
	 * @param cacheData
	 * @param raEntity
	 */
	public ReplicatedDataImpl(String name, String raEntity,
			MobicentsCluster cluster, FaultTolerantResourceAdaptor<K, V> ra,
			boolean activateDataRemovedCallback) {
		cacheData = new ReplicatedDataCacheData<K, V>(name, raEntity,
				cluster);
		cacheData.create();
		this.cluster = cluster;
		if (activateDataRemovedCallback) {
			this.dataRemovalListener = new DataRemovalListener<K, V>(ra, getCacheData());
			cluster.addDataRemovalListener(dataRemovalListener);
		}
		else {
			dataRemovalListener = null;
		}
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
		if (dataRemovalListener != null) {
			cluster.removeDataRemovalListener(dataRemovalListener);
		}
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
