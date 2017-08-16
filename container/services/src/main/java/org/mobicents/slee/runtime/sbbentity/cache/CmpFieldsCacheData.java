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

import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class CmpFieldsCacheData extends ClusteredCacheData<SbbEntityCacheKey,HashMap<String,Object>> 
{
	public CmpFieldsCacheData(SbbEntityID sbbEntityID, MobicentsCache cache) {
		super(new SbbEntityCacheKey(sbbEntityID, SbbEntityCacheType.CMP_FIELDS), cache);		
	}
	
	public Boolean setField(Boolean createIfNotExists,String name,Object value) {
		HashMap<String, Object> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<String, Object>();
		
		if(map!=null) {
			map=new HashMap<String, Object>(map);
			map.put(name, value);
			super.putValue(map);
			return true;
		}
		else
			return false;
	}
	
	public Boolean removeField(Boolean createIfNotExists,String name) {
		HashMap<String, Object> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<String, Object>();
		
		if(map!=null) {
			if(map.containsKey(name)) {
				map.remove(name);
				super.putValue(map);
				return true;
			}
			else {
				return false;
			}
		}
		else
			return false;
	}
	
	public Object getField(Boolean createIfNotExists,String name) {
		HashMap<String, Object> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Object>();
			super.putValue(map);
			return null;
		}
		
		if(map!=null) {
			return map.get(name);
		}
		
		return null;
	}
	
	public HashMap<String,Object> getFields(Boolean createIfNotExists) {
		HashMap<String, Object> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Object>();
			super.putValue(map);
			return map;
		}
		
		if(map!=null) {
			return map;
		}
		
		return new HashMap<String,Object>();
	}
	
	public void removeNode() {
		super.remove();
	}
}