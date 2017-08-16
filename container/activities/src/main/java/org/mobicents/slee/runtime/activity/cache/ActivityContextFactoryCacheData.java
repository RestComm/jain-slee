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

package org.mobicents.slee.runtime.activity.cache;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.restcomm.cluster.MobicentsCluster;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextFactoryCacheData {

	private MobicentsCluster cluster;
	
	/**
	 * 
	 * @param cluster
	 */
	public ActivityContextFactoryCacheData(MobicentsCluster cluster) {
		this.cluster=cluster;
	}

	/**
	 * Retrieves a set containing all activity context handles in the factory's
	 * cache data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<ActivityContextHandle> getActivityContextHandles() {
		//WARNING : EXPENSIVE OPERATION , CONSIDER OPTIMIZATION
		Set<ActivityCacheKey> keys=(Set<ActivityCacheKey>)this.cluster.getMobicentsCache().getAllKeys();
		Set<ActivityContextHandle> handles=new HashSet<ActivityContextHandle>();
		Iterator<ActivityCacheKey> keysIterator=keys.iterator();
		while(keysIterator.hasNext()) {
			ActivityCacheKey curr=keysIterator.next();
			if(curr.getType()==ActivityCacheType.METADATA)
				handles.add(curr.getActivityHandle());
		}
			
		return handles;
	}

	/**
	 * Retrieves a set containing all activity context handles in the factory's
	 * cache data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<ActivityContextHandle> getActivityContextHandlesByType(ActivityType type) {
		//WARNING : EXPENSIVE OPERATION , CONSIDER OPTIMIZATION
		Set<ActivityCacheKey> keys=(Set<ActivityCacheKey>)this.cluster.getMobicentsCache().getAllKeys();
		Set<ActivityContextHandle> handles=new HashSet<ActivityContextHandle>();
		Iterator<ActivityCacheKey> keysIterator=keys.iterator();
		while(keysIterator.hasNext()) {
			ActivityCacheKey curr=keysIterator.next();
			if(curr.getType()==ActivityCacheType.METADATA && curr.getActivityHandle().getActivityType()==type)
				handles.add(curr.getActivityHandle());
		}
			
		return handles;
	}
}