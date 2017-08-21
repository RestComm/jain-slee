package org.mobicents.slee.runtime.activity.cache;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class IsEndingCacheData extends ClusteredCacheData<ActivityCacheKey,Boolean> 
{
	public IsEndingCacheData(ActivityContextHandle handle, MobicentsCache cache) {
		super(new ActivityCacheKey(handle, ActivityCacheType.ENDING), cache);		
	}
	
	public void set(Boolean isEnding) {
		super.putValue(isEnding);		
	}
	
	public Boolean removeElement() {
		return super.removeElement();		
	}	
	
	public Boolean getElement() {
		return super.getValue();
	}
}