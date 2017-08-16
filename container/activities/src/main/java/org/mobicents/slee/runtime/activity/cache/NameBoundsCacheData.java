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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class NameBoundsCacheData extends ClusteredCacheData<ActivityCacheKey,HashMap<String,Void>> 
{
	public NameBoundsCacheData(ActivityContextHandle handle, MobicentsCache cache) {
		super(new ActivityCacheKey(handle, ActivityCacheType.NAMES_BOUND), cache);		
	}
	
	public Boolean bind(Boolean createIfNotExists,String name) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<String, Void>();
		
		if(map!=null) {
			if(map.containsKey(name)) {
				return false;
			}
			else {
				map=new HashMap<String, Void>(map);
				map.put(name, null);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean unbind(Boolean createIfNotExists,String name) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Void>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(name))
				return false;
			else {
				map=new HashMap<String, Void>(map);
				map.remove(name);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean hasNameBounds(Boolean createIfNotExists) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Void>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			return map.size()!=0;
		}
		
		return false;
	}
	
	public Set<String> getNameBounds(Boolean createIfNotExists) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Void>();
			super.putValue(map);
			return map.keySet();
		}
		
		if(map!=null) {
			return map.keySet();
		}
		
		return new HashSet<String>();
	}
	
	public void removeNode() {
		super.remove();
	}
}