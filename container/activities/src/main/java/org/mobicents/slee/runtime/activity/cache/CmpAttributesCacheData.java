package org.mobicents.slee.runtime.activity.cache;

import java.util.HashMap;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class CmpAttributesCacheData extends ClusteredCacheData<ActivityCacheKey,HashMap<String,Object>> 
{
	public CmpAttributesCacheData(ActivityContextHandle handle, MobicentsCache cache) {
		super(new ActivityCacheKey(handle, ActivityCacheType.CMP_ATTRIBUTES), cache);		
	}
	
	public Boolean setAttribute(Boolean createIfNotExists,String name,Object value) {
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
	
	public Boolean removeAttribute(Boolean createIfNotExists,String name) {
		HashMap<String, Object> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<String, Object>();
		
		if(map!=null) {
			if(map.containsKey(name)) {
				map=new HashMap<String, Object>(map);
				map.remove(name);
				super.putValue(map);
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public Object getAttribute(Boolean createIfNotExists,String name) {
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
	
	public HashMap<String,Object> getAttributes(Boolean createIfNotExists) {
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