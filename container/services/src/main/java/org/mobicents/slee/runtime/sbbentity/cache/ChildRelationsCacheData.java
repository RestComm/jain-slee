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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class ChildRelationsCacheData extends ClusteredCacheData<SbbEntityCacheKey,HashMap<SbbEntityID,Void>> 
{
	public ChildRelationsCacheData(SbbEntityID sbbEntityID, MobicentsCache cache) {
		super(new SbbEntityCacheKey(sbbEntityID, SbbEntityCacheType.CHILD_RELATIONS), cache);		
	}
	
	public Set<SbbEntityID> getAllChildSbbEntities(Boolean createIfNotExists) {
		HashMap<SbbEntityID, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<SbbEntityID, Void>();
		
		if(map!=null) {
			Set<SbbEntityID> output=new HashSet<SbbEntityID>();
			output.addAll(map.keySet());
			return output;
		}
		else
			return new HashSet<SbbEntityID>();
	}
	
	public Set<SbbEntityID> getChildRelationSbbEntities(Boolean createIfNotExists,String getChildRelationMethod) {
		HashMap<SbbEntityID, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<SbbEntityID, Void>();
		
		if(map!=null) {
			Set<SbbEntityID> output=new HashSet<SbbEntityID>();
			Iterator<SbbEntityID> keys=map.keySet().iterator();
			while(keys.hasNext()) {
				SbbEntityID curr=keys.next();
				if(curr.getParentChildRelation()!=null && curr.getParentChildRelation().equals(getChildRelationMethod))
					output.add(curr);
			}
			
			return output;
		}
		else
			return new HashSet<SbbEntityID>();
	}
}