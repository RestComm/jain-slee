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
import java.util.Set;

import org.jboss.cache.Fqn;
import org.mobicents.cache.CacheData;
import org.mobicents.cluster.MobicentsCluster;

/**
 * @author martins
 * 
 */
public class ReplicatedDataCacheData<K extends Serializable, V extends Serializable>
		extends CacheData {

	/**
	 * 
	 * @param name
	 * @param raEntity
	 * @param mobicentsCluster
	 */
	public ReplicatedDataCacheData(String rootName, String raEntity,
			MobicentsCluster mobicentsCluster) {
		super(Fqn.fromElements(rootName, raEntity), mobicentsCluster
				.getMobicentsCache());
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<K> getAllKeys() {
		return getNode().getChildrenNames();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key) {
		return getNode().hasChild(key);
	}
}
