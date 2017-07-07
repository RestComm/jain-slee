package org.mobicents.slee.runtime.activity.cache;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.CacheDataExecutorService;
import org.restcomm.cache.MobicentsCache;

public class IsEndingCacheData extends CacheData<ActivityCacheKey,Boolean> 
{
	public IsEndingCacheData(ActivityContextHandle handle, MobicentsCache cache, CacheDataExecutorService cacheExecutorService) {
		super(new ActivityCacheKey(handle, ActivityCacheType.ENDING), cache, cacheExecutorService);		
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