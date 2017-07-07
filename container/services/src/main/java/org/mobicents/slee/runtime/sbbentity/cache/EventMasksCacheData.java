package org.mobicents.slee.runtime.sbbentity.cache;

import java.util.HashMap;
import java.util.Set;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.CacheDataExecutorService;
import org.restcomm.cache.MobicentsCache;

public class EventMasksCacheData extends CacheData<SbbEntityCacheKey,HashMap<ActivityContextHandle,Set<EventTypeID>>> 
{
	public EventMasksCacheData(SbbEntityID handle, MobicentsCache cache, CacheDataExecutorService cacheExecutorService) {
		super(new SbbEntityCacheKey(handle, SbbEntityCacheType.MASKED_EVENTS), cache, cacheExecutorService);		
	}
	
	public Boolean setEventMask(Boolean createIfNotExists,ActivityContextHandle handle,Set<EventTypeID> mask) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.get();
		if(map==null && createIfNotExists)
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
		
		if(map!=null) {
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
			map.put(handle, mask);
			super.put(map);
			return true;
		}
		else
			return false;
	}
	
	public Boolean updateEventMask(Boolean createIfNotExists,ActivityContextHandle handle,Set<EventTypeID> mask) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.get();
		if(map==null && createIfNotExists)
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
		if(map!=null) {
			if(map.containsKey(handle)) {
				map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
				map.get(handle).addAll(mask);
				super.put(map);
				return true;
			}
			else {
				map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
				map.put(handle, mask);
				super.put(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean removeMask(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
			super.put(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(handle))
				return false;
			else {
				map=new HashMap<ActivityContextHandle, Set<EventTypeID>>(map);
				map.remove(handle);
				super.put(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Set<EventTypeID> getMask(Boolean createIfNotExists,ActivityContextHandle handle) {
		HashMap<ActivityContextHandle, Set<EventTypeID>> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<ActivityContextHandle, Set<EventTypeID>>();
			super.put(map);
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