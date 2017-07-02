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