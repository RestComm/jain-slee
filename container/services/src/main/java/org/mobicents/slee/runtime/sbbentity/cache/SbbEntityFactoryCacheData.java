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

package org.mobicents.slee.runtime.sbbentity.cache;

import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cluster.MobicentsCluster;

import javax.slee.ServiceID;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class SbbEntityFactoryCacheData {

	private MobicentsCluster cluster;
	/**
	 * 
	 * @param cluster
	 */
	public SbbEntityFactoryCacheData(MobicentsCluster cluster) {
		this.cluster=cluster;
	}

	/**
	 * Retrieves a set containing sbb entity ids in the factory
	 * cache data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<SbbEntityID> getSbbEntities() {
		//WARNING : EXPENSIVE OPERATION , CONSIDER OPTIMIZATION
		Set<SbbEntityCacheKey> keys=(Set<SbbEntityCacheKey>)this.cluster.getMobicentsCache().getAllKeys();
		Set<SbbEntityID> sbbEntityIDs=new HashSet<SbbEntityID>();
		Iterator<SbbEntityCacheKey> keysIterator=keys.iterator();
		while(keysIterator.hasNext()) {
			SbbEntityCacheKey curr=keysIterator.next();
			if(curr.getType()==SbbEntityCacheType.METADATA)
				sbbEntityIDs.add(curr.getSbbEntityID());
		}
			
		return sbbEntityIDs;
	}	

	@SuppressWarnings("unchecked")
	public Set<SbbEntityID> getRootSbbEntityIDs(ServiceID serviceID) {
		//WARNING : EXPENSIVE OPERATION , CONSIDER OPTIMIZATION
		Set<SbbEntityCacheKey> keys=(Set<SbbEntityCacheKey>)this.cluster.getMobicentsCache().getAllKeys();
		Set<SbbEntityID> sbbEntityIDs=new HashSet<SbbEntityID>();
		Iterator<SbbEntityCacheKey> keysIterator=keys.iterator();
		while(keysIterator.hasNext()) {
			SbbEntityCacheKey curr=keysIterator.next();
			if(curr.getType()==SbbEntityCacheType.METADATA && curr.getSbbEntityID().getServiceID().equals(serviceID)) {
				sbbEntityIDs.add(curr.getSbbEntityID());
			}			
		}
			
		return sbbEntityIDs;
	}	
}