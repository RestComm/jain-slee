package org.mobicents.slee.runtime.activity.cache;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.MobicentsCache;

public class IsEndingCacheData extends CacheData<ActivityCacheKey,Boolean> 
{
	public IsEndingCacheData(ActivityContextHandle handle, MobicentsCache cache) {
		super(new ActivityCacheKey(handle, ActivityCacheType.ENDING), cache);		
	}
	
	public void set(Boolean isEnding) {
		super.put(isEnding);		
	}
	
	public Boolean remove() {
		return super.remove();		
	}	
	
	public Boolean get() {
		return super.get();
	}
}