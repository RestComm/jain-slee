package org.mobicents.slee.runtime.activity.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class NameBoundsCacheData extends ClusteredCacheData<ActivityCacheKey,HashMap<String,Void>> 
{
	public NameBoundsCacheData(ActivityContextHandle handle, MobicentsCache cache) {
		super(new ActivityCacheKey(handle, ActivityCacheType.NAMES_BOUND), cache);		
	}
	
	public Boolean bind(Boolean createIfNotExists,String name) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<String, Void>();
		
		if(map!=null) {
			if(map.containsKey(name)) {
				return false;
			}
			else {
				map=new HashMap<String, Void>(map);
				map.put(name, null);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean unbind(Boolean createIfNotExists,String name) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Void>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			if(!map.containsKey(name))
				return false;
			else {
				map=new HashMap<String, Void>(map);
				map.remove(name);
				super.putValue(map);
				return true;
			}
		}
		else
			return false;
	}
	
	public Boolean hasNameBounds(Boolean createIfNotExists) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Void>();
			super.putValue(map);
			return false;
		}
		
		if(map!=null) {
			return map.size()!=0;
		}
		
		return false;
	}
	
	public Set<String> getNameBounds(Boolean createIfNotExists) {
		HashMap<String, Void> map=super.getValue();
		if(map==null && createIfNotExists) {			
			map=new HashMap<String, Void>();
			super.putValue(map);
			return map.keySet();
		}
		
		if(map!=null) {
			return map.keySet();
		}
		
		return new HashSet<String>();
	}
	
	public void removeNode() {
		super.remove();
	}
}