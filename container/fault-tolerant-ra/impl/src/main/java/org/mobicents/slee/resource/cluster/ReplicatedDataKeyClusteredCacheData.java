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

import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

/**
 * @author martins
 * @author oifa.yulian
 * 
 */
public class ReplicatedDataKeyClusteredCacheData<K extends Serializable, V extends Serializable>
		extends ClusteredCacheData<K,V> {

	/**
	 * @param parent
	 * @param key
	 * @param mobicentsCache
	 */
	public ReplicatedDataKeyClusteredCacheData(K key,MobicentsCache mobicentsCache) {
		super(key,mobicentsCache);
	}

	public V getValue() {
		return super.getValue();		
	}

	public V setValue(V value) {
		return super.putValue(value);			
	}

}
