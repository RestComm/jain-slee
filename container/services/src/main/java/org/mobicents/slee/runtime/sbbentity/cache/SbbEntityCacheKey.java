package org.mobicents.slee.runtime.sbbentity.cache;

import java.io.Serializable;

import org.mobicents.slee.container.sbbentity.SbbEntityID;

public class SbbEntityCacheKey implements Serializable
{
	private static final long serialVersionUID = 4080743323394471546L;

	private SbbEntityID sbbEntityID;
	private SbbEntityCacheType type;
	
	public SbbEntityCacheKey(SbbEntityID handle,SbbEntityCacheType type) {
		this.sbbEntityID=handle;
		this.type=type;
	}

	public SbbEntityID getSbbEntityID() {
		return sbbEntityID;
	}

	public SbbEntityCacheType getType() {
		return type;
	}	
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof SbbEntityCacheKey))
			return false;
		
		SbbEntityCacheKey otherKey=(SbbEntityCacheKey)other;
		
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
		
	    return result;
	}
}