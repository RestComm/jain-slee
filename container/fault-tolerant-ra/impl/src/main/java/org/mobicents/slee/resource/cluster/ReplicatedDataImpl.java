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

import org.infinispan.CacheSet;
import org.infinispan.remoting.transport.Address;
import org.restcomm.cluster.MobicentsCluster;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	private final DataRemovalListener<K, V> dataRemovalListener;
	
	/**
	 * @param name
	 * @param raEntity
	 * @param cluster
	 * @param ra
	 * @param activateDataRemovedCallback
	 */
	public ReplicatedDataImpl(String name, String raEntity,
			MobicentsCluster cluster, FaultTolerantResourceAdaptor<K, V> ra,
			boolean activateDataRemovedCallback) {
		this.cluster = cluster;
		if (activateDataRemovedCallback) {
			this.dataRemovalListener = new DataRemovalListener<K, V>(ra);
			cluster.addDataRemovalListener(dataRemovalListener);
		}
		else {
			dataRemovalListener = null;
		}
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
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#getLocalKeyset()
	 */
	public Set<K> getLocalKeyset() {
		Set<K> set = new HashSet<K>();
		ReplicatedDataKeyClusteredCacheData<K, V> handleCacheData = null;
		Address handleCacheDataClusterNode = null;
		@SuppressWarnings("unchecked")
		Set<K> keys=(Set<K>)this.cluster.getMobicentsCache().getAllKeys();
		for(Object currKey:keys) {
			@SuppressWarnings("unchecked")
			K localKey=(K)currKey;
			handleCacheData = new ReplicatedDataKeyClusteredCacheData<K, V>(localKey, cluster.getMobicentsCache());
			handleCacheDataClusterNode = handleCacheData
					.getClusterNodeAddress();
			if (handleCacheDataClusterNode == null
					|| handleCacheDataClusterNode.equals(cluster
							.getLocalAddress())) {
				set.add(localKey);
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
				key, cluster.getMobicentsCache());
		V original=keyCacheData.setValue(value);
		return original!=null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#get(java.io.Serializable)
	 */
	public V get(K key) {
		final ReplicatedDataKeyClusteredCacheData<K, V> handleCacheData = new ReplicatedDataKeyClusteredCacheData<K, V>(
				key, cluster.getMobicentsCache());
		return handleCacheData.getValue();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#contains(java.io.Serializable)
	 */
	public boolean contains(K key) {
		return new ReplicatedDataKeyClusteredCacheData<K, V>(key,
				cluster.getMobicentsCache()).exists();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#remove(java.io.Serializable)
	 */
	public boolean remove(K key) {
		return new ReplicatedDataKeyClusteredCacheData<K, V>(key, cluster.getMobicentsCache()).removeElement()!=null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedData#getKeyset()
	 */
	@SuppressWarnings("unchecked")
	public Set<K> getKeyset() {
		return (Set<K>)this.cluster.getMobicentsCache().getAllKeys();		
	}
}
