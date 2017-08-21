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