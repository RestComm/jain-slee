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
import java.util.Set;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class EventMasksCacheData extends ClusteredCacheData<SbbEntityCacheKey,HashMap<ActivityContextHandle,Set<EventTypeID>>> 
{
	public EventMasksCacheData(SbbEntityID handle, MobicentsCache cache) {
		super(new SbbEntityCacheKey(handle, SbbEntityCacheType.MASKED_EVENTS), cache);		
	}
	
	public Boolean setEventMask(Boolean createIfNotExists,ActivityContextHandle handle,Set<EventTypeID> mask) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
		
		if(map!=null) {
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
			map.put(handle, mask);
			super.putValue(map);
			return true;
		}
		else
			return false;
	}
	
	public Boolean updateEventMask(Boolean createIfNotExists,ActivityContextHandle handle,Set<EventTypeID> mask) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
		if(map!=null) {
			if(map.containsKey(handle)) {
				map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
				map.get(handle).addAll(mask);
				super.putValue(map);
				return true;
			}
			else {
				map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
				map.put(handle, mask);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean removeMask(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(handle))
				return false;
			else {
				map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
				map.remove(handle);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Set<EventTypeID> getMask(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
			super.putValue(map);
			return null;
		}
		
		if(map!=null) {
			return map.get(handle);
		}
		
		return null;
	}
	
	public void removeNode() {
		super.remove();
	}
}