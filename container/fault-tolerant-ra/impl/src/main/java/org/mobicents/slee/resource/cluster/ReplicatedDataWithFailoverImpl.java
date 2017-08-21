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

import java.io.Serializable;

import org.restcomm.cluster.MobicentsCluster;

/**
 * @author martins
 * 
 */
public class ReplicatedDataWithFailoverImpl<K extends Serializable, V extends Serializable>
		extends ReplicatedDataImpl<K, V> implements
		ReplicatedDataWithFailover<K, V> {

	/**
	 * 
	 */
	private final FailOverListener<K, V> clientLocalListener;

	/**
	 * @param name
	 * @param raEntity
	 * @param cluster
	 */
	public ReplicatedDataWithFailoverImpl(String name, String raEntity,
			MobicentsCluster cluster, FaultTolerantResourceAdaptor<K, V> ra,boolean activateDataRemovedCallback) {
		super(name, raEntity, cluster, ra, activateDataRemovedCallback);
		clientLocalListener = new FailOverListener<K, V>(ra);
		cluster.addFailOverListener(clientLocalListener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedDataImpl#remove()
	 */
	@Override
	public void remove() {
		getCluster().removeFailOverListener(clientLocalListener);
		super.remove();
	}
}
