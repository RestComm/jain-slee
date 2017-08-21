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

import java.io.Serializable;

import org.mobicents.slee.container.activity.ActivityContextHandle;

public class ActivityCacheKey implements Serializable
{
	private static final long serialVersionUID = 6480010493858490671L;
	
	private ActivityContextHandle activityHandle;
	private ActivityCacheType type;
	
	public ActivityCacheKey(ActivityContextHandle handle,ActivityCacheType type) {
		this.activityHandle=handle;
		this.type=type;
	}

	public ActivityContextHandle getActivityHandle() {
		return activityHandle;
	}

	public ActivityCacheType getType() {
		return type;
	}	
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof ActivityCacheKey))
			return false;
		
		ActivityCacheKey otherKey=(ActivityCacheKey)other;
		
		if(otherKey.getActivityHandle()==null) {
			if(activityHandle!=null)
				return false;
		} else {
			if(!activityHandle.equals(otherKey.getActivityHandle()))
				return false;
		}
		
		if(otherKey.getType()==null) {
			if(type!=null)
				return false;
		} else {
			if(!type.equals(otherKey.getType()))
				return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result=0;
		if(activityHandle!=null) {
			result=activityHandle.hashCode();
		}
		
		if(type!=null) {
			result = result * 31 + type.hashCode();
		}
		
	    return result;
	}
}