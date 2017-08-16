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

/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import org.infinispan.remoting.transport.Address;
import org.restcomm.cluster.cache.ClusteredCacheData;
import org.restcomm.cluster.election.ClientLocalListenerElector;

import java.io.Serializable;

/**
 * @author martins
 * 
 */
public class FailOverListener<K extends Serializable, V extends Serializable>
		implements org.restcomm.cluster.FailOverListener {

	private final FaultTolerantResourceAdaptor<K, V> ra;
	
	/**
	 * @param ra
	 * @param baseCacheData
	 */
	public FailOverListener(FaultTolerantResourceAdaptor<K, V> ra) {
		this.ra = ra;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.restcomm.cluster.ClientLocalListener#failOverClusterMember(org.jgroups
	 * .Address)
	 */
	public void failOverClusterMember(Address address) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * @see org.restcomm.cluster.FailOverListener#getElector()
	 */
	public ClientLocalListenerElector getElector() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restcomm.cluster.ClientLocalListener#getPriority()
	 */
	public byte getPriority() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.restcomm.cluster.ClientLocalListener#lostOwnership(org.mobicents
	 * .cluster.cache.ClusteredCacheData)
	 */
	public void lostOwnership(@SuppressWarnings("rawtypes") ClusteredCacheData clusteredCacheData) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.restcomm.cluster.ClientLocalListener#wonOwnership(org.mobicents.
	 * cluster.cache.ClusteredCacheData)
	 */
	@SuppressWarnings("unchecked")
	public void wonOwnership(@SuppressWarnings("rawtypes") ClusteredCacheData clusteredCacheData) {
		ra.failOver((K) clusteredCacheData.getValue());
	}
}
