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

import org.jboss.cache.Fqn;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.cluster.cache.ClusteredCacheData;

/**
 * @author martins
 * 
 */
public class ReplicatedDataKeyClusteredCacheData<K extends Serializable, V extends Serializable>
		extends ClusteredCacheData {

	private final static Boolean DATA_MAP_KEY = Boolean.TRUE;

	/**
	 * @param nodeFqn
	 * @param mobicentsCluster
	 */
	@SuppressWarnings("unchecked")
	public ReplicatedDataKeyClusteredCacheData(
			ReplicatedDataCacheData parent, K key,
			MobicentsCluster mobicentsCluster) {
		super(Fqn.fromRelativeElements(parent.getNodeFqn(), key),
				mobicentsCluster);
	}

	@SuppressWarnings("unchecked")
	public V getValue() {
		return (V) getNode().get(DATA_MAP_KEY);
	}

	@SuppressWarnings("unchecked")
	public void setValue(V value) {
		getNode().put(DATA_MAP_KEY, value);
	}

}
