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
import java.util.Set;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class AttachedActivityContextsCacheData extends ClusteredCacheData<SbbEntityCacheKey,HashMap<ActivityContextHandle,Void>> 
{
	public AttachedActivityContextsCacheData(SbbEntityID handle, MobicentsCache cache) {
		super(new SbbEntityCacheKey(handle, SbbEntityCacheType.ATTACHED_CONTEXTS), cache);		
	}
	
	public Boolean attachHandle(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<ActivityContextHandle, Void>();
		
		if(map!=null) {
			if(map.containsKey(handle)) {
				return false;
			}
			else {
				map=new HashMap<ActivityContextHandle, Void>(map);
				map.put(handle, null);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean detachHandle(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Void>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(handle))
				return false;
			else {
				map=new HashMap<ActivityContextHandle, Void>(map);
				map.remove(handle);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean hasHandle(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Void>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			return map.containsKey(handle);
		}
		
		return false;
	}
	
	public Set<ActivityContextHandle> getHandles(Boolean createIfNotExists) {
		HashMap<ActivityContextHandle, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Void>();
			super.putValue(map);
			return map.keySet();
		}
		
		if(map!=null) {
			return map.keySet();
		}
		
		return new HashSet<ActivityContextHandle>();
	}
	
	public void removeNode() {
		super.remove();
	}
}