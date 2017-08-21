package org.mobicents.slee.runtime.sbbentity.cache;

import org.mobicents.slee.container.sbbentity.SbbEntityID;

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

public class SbbEntityCmpFieldCacheKey 
{
	private SbbEntityID sbbEntityID;
	private SbbEntityCacheType type;
	private Object key;
	
	public SbbEntityCmpFieldCacheKey(SbbEntityID handle,SbbEntityCacheType type,Object key) {
		this.sbbEntityID=handle;
		this.type=type;
		this.key=key;
	}

	public SbbEntityID getSbbEntityID() {
		return sbbEntityID;
	}

	public SbbEntityCacheType getType() {
		return type;
	}	
	
	public Object getKey() {
		return key;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof SbbEntityCmpFieldCacheKey))
			return false;
		
		SbbEntityCmpFieldCacheKey otherKey=(SbbEntityCmpFieldCacheKey)other;
		
		if(otherKey.getSbbEntityID()==null) {
			if(sbbEntityID!=null)
				return false;
		} else {
			if(!sbbEntityID.equals(otherKey.getSbbEntityID()))
				return false;
		}
		
		if(otherKey.getType()==null) {
			if(type!=null)
				return false;
		} else {
			if(!type.equals(otherKey.getType()))
				return false;
		}
		
		if(otherKey.getKey()==null) {
			if(key!=null)
				return false;
		} else {
			if(!key.equals(otherKey.getKey()))
				return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result=0;
		if(sbbEntityID!=null) {
			result=sbbEntityID.hashCode();
		}
		
		if(type!=null) {
			result = result * 31 + type.hashCode();
		}
		
		if(key!=null) {
			result = result * 31 + key.hashCode();
		}
		
	    return result;
	}
}