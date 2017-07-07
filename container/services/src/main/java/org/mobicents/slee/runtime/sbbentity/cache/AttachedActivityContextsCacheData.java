package org.mobicents.slee.runtime.sbbentity.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.CacheDataExecutorService;
import org.restcomm.cache.MobicentsCache;

public class AttachedActivityContextsCacheData extends CacheData<SbbEntityCacheKey,HashMap<ActivityContextHandle,Void>> 
{
	public AttachedActivityContextsCacheData(SbbEntityID handle, MobicentsCache cache, CacheDataExecutorService cacheExecutorService) {
		super(new SbbEntityCacheKey(handle, SbbEntityCacheType.ATTACHED_CONTEXTS), cache, cacheExecutorService);		
	}
	
	public Boolean attachHandle(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Void> map=super.get();
		if(map==null && createIfNotExists)
			map=new HashMap<ActivityContextHandle, Void>();
		
		if(map!=null) {
			if(map.containsKey(handle)) {
				return false;
			}
			else {
				map=new HashMap<ActivityContextHandle, Void>(map);
				map.put(handle, null);
				super.put(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean detachHandle(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Void> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Void>();
			super.put(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(handle))
				return false;
			else {
				map=new HashMap<ActivityContextHandle, Void>(map);
				map.remove(handle);
				super.put(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean hasHandle(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Void> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Void>();
			super.put(map);
			return false;
		}
		
		if(map!=null) {
			return map.containsKey(handle);
		}
		
		return false;
	}
	
	public Set<ActivityContextHandle> getHandles(Boolean createIfNotExists) {
		HashMap<ActivityContextHandle, Void> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Void>();
			super.put(map);
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