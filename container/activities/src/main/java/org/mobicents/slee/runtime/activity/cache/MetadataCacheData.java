package org.mobicents.slee.runtime.activity.cache;

import java.util.HashMap;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.CacheDataExecutorService;
import org.restcomm.cache.MobicentsCache;

public class MetadataCacheData extends CacheData<ActivityCacheKey,HashMap<String,Object>> 
{
	public MetadataCacheData(ActivityContextHandle handle, MobicentsCache cache, CacheDataExecutorService cacheExecutorService) {
		super(new ActivityCacheKey(handle, ActivityCacheType.METADATA), cache, cacheExecutorService);		
	}
	
	public Boolean setObject(Boolean createIfNotExists,String name,Object value) {
		HashMap<String, Object> map=super.get();
		if(map==null && createIfNotExists)
			map=new HashMap<String, Object>();
		
		if(map!=null) {
			map=new HashMap<String, Object>(map);
			map.put(name, value);
			super.put(map);
			return true;
		}
		else
			return false;
	}
	
	public Boolean removeObject(Boolean createIfNotExists,String name) {
		HashMap<String, Object> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Object>();
			super.put(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(name))
				return false;
			else {
				map=new HashMap<String, Object>(map);
				map.remove(name);
				super.put(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Object getObject(Boolean createIfNotExists,String name) {
		HashMap<String, Object> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Object>();
			super.put(map);
			return null;
		}
		
		if(map!=null) {
			return map.get(name);
		}
		
		return null;
	}
	
	public HashMap<String,Object> getAll(Boolean createIfNotExists) {
		HashMap<String, Object> map=super.get();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Object>();
			super.put(map);
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