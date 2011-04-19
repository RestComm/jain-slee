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

import org.mobicents.cluster.MobicentsCluster;

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
		clientLocalListener = new FailOverListener<K, V>(ra, getCacheData());
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
