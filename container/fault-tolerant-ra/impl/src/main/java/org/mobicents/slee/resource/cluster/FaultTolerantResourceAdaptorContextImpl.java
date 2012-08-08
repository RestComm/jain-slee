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

package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import org.jgroups.Address;
import org.mobicents.slee.container.SleeContainer;

/**
 * Implementation of the fault tolerant ra context.
 * 
 * @author martins
 * 
 */
public class FaultTolerantResourceAdaptorContextImpl<K extends Serializable, V extends Serializable>
		implements FaultTolerantResourceAdaptorContext<K, V> {

	private static final String REPLICATED_DATA_WITH_FAILOVER_NAME = "ra-data-fo";
	private static final String REPLICATED_DATA_NAME = "ra-data";

	private ReplicatedDataWithFailoverImpl<K, V> replicatedDataWithFailover = null;
	private ReplicatedDataImpl<K, V> replicatedData = null;

	private final String raEntity;
	private final SleeContainer sleeContainer;
	private final FaultTolerantResourceAdaptor<K, V> ra;

	private FaultTolerantTimerImpl timer;

	/**
	 * @param raEntity
	 * @param sleeContainer
	 * @param ra
	 */
	public FaultTolerantResourceAdaptorContextImpl(String raEntity,
			SleeContainer sleeContainer, FaultTolerantResourceAdaptor<K, V> ra) {
		this.raEntity = raEntity;
		this.sleeContainer = sleeContainer;
		this.ra = ra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #getReplicatedDataWithFailover(boolean)
	 */
	public ReplicatedDataWithFailover<K, V> getReplicatedDataWithFailover(
			boolean activateDataRemovedCallback) {
		if (replicatedDataWithFailover == null) {
			replicatedDataWithFailover = new ReplicatedDataWithFailoverImpl<K, V>(
					REPLICATED_DATA_WITH_FAILOVER_NAME, raEntity,
					sleeContainer.getCluster(), ra, activateDataRemovedCallback);
		}
		return replicatedDataWithFailover;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #getReplicateData(boolean)
	 */
	public ReplicatedData<K, V> getReplicateData(
			boolean activateDataRemovedCallback) {
		if (replicatedData == null) {
			replicatedData = new ReplicatedDataImpl<K, V>(REPLICATED_DATA_NAME,
					raEntity, sleeContainer.getCluster(), ra,
					activateDataRemovedCallback);
		}
		return replicatedData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #isLocal()
	 */
	public boolean isLocal() {
		return sleeContainer.getCluster().getMobicentsCache().isLocalMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #isHeadMember()
	 */
	public boolean isHeadMember() {
		return sleeContainer.getCluster().isHeadMember();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #isSingleMember()
	 */
	public boolean isSingleMember() {
		return sleeContainer.getCluster().isSingleMember();
	}

	private static final Address[] EMPTY_ADDRESS_ARRAY = {};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #getMembers()
	 */
	public MemberAddress[] getMembers() {
		final Address[] addresses = sleeContainer.getCluster()
				.getClusterMembers().toArray(EMPTY_ADDRESS_ARRAY);
		final MemberAddressImpl[] members = new MemberAddressImpl[addresses.length];
		for (int i = 0; i < members.length; i++) {
			members[i] = new MemberAddressImpl(addresses[i]);
		}
		return members;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext
	 * #getLocalAddress()
	 */
	public MemberAddress getLocalAddress() {
		Address localAddress = sleeContainer.getCluster().getLocalAddress();
		return localAddress != null ? new MemberAddressImpl(localAddress)
				: null;
	}

	/**
	 * Removes all replicated data
	 */
	public void removeReplicateData() {
		if (replicatedDataWithFailover != null) {
			replicatedDataWithFailover.remove();
			replicatedDataWithFailover = null;
		}
		if (replicatedData != null) {
			replicatedData.remove();
			replicatedData = null;
		}
	}

	@Override
	public FaultTolerantTimer getFaultTolerantTimer() {
		if (timer == null) {
			timer = new FaultTolerantTimerImpl(sleeContainer, raEntity);
		}
		return timer;
	}

	public void shutdown() {
		if (timer != null) {
			timer.shutdown();
		}
	}
}
