/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

/**
 * Fault tolerant context for a {@link FaultTolerantResourceAdaptor}, gives
 * access to replicated data and information of the SLEE cluster.
 * 
 * @author martins
 * 
 */
public interface FaultTolerantResourceAdaptorContext<K extends Serializable, V extends Serializable> {

	/**
	 * Retrieves the address of the local node or null if not running in cluster
	 * mode.
	 * 
	 * @return
	 */
	public MemberAddress getLocalAddress();

	/**
	 * Retrieves the members of the cluster.
	 * 
	 * @return
	 */
	public MemberAddress[] getMembers();

	/**
	 * Indicates if it is the head member of the cluster.
	 * 
	 * @return true if isLocal() or the node is the first member of the cluster.
	 */
	public boolean isHeadMember();

	/**
	 * Indicates if the node is the single member of the cluster.
	 * 
	 * @return true if isLocal() or the cluster has a single member.
	 */
	public boolean isSingleMember();

	/**
	 * Indicates if the resource adaptor object is running in a local or cluster
	 * config.
	 * 
	 * @return
	 */
	public boolean isLocal();

	/**
	 * Retrieves the {@link ReplicatedData} for the
	 * {@link FaultTolerantResourceAdaptor}
	 * 
	 * @param activateDataRemovedCallback
	 * @return
	 */
	public ReplicatedData<K, V> getReplicateData(
			boolean activateDataRemovedCallback);

	/**
	 * Retrieves the {@link ReplicatedDataWithFailover} for the
	 * {@link FaultTolerantResourceAdaptor}
	 * 
	 * @param activateDataRemovedCallback
	 * @return
	 */
	public ReplicatedDataWithFailover<K, V> getReplicatedDataWithFailover(
			boolean activateDataRemovedCallback);

	/**
	 * Retrieves the RA Entity fault tolerant timer, which may be used to
	 * schedule the execution of fault tolerant timer tasks.
	 * <p>
	 * Each RA Entity has a single fault tolerant timer.
	 * 
	 * @return
	 */
	public FaultTolerantTimer getFaultTolerantTimer();

}
