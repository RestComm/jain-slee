package org.mobicents.slee.runtime.sbbentity.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class ChildRelationsCacheData extends ClusteredCacheData<SbbEntityCacheKey,HashMap<SbbEntityID,Void>> 
{
	public ChildRelationsCacheData(SbbEntityID sbbEntityID, MobicentsCache cache) {
		super(new SbbEntityCacheKey(sbbEntityID, SbbEntityCacheType.CHILD_RELATIONS), cache);		
	}
	
	public Set<SbbEntityID> getAllChildSbbEntities(Boolean createIfNotExists) {
		HashMap<SbbEntityID, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<SbbEntityID, Void>();
		
		if(map!=null) {
			Set<SbbEntityID> output=new HashSet<SbbEntityID>();
			output.addAll(map.keySet());
			return output;
		}
		else
			return new HashSet<SbbEntityID>();
	}
	
	public Set<SbbEntityID> getChildRelationSbbEntities(Boolean createIfNotExists,String getChildRelationMethod) {
		HashMap<SbbEntityID, Void> map=super.getValue();
		if(map==null && createIfNotExists)
			map=new HashMap<SbbEntityID, Void>();
		
		if(map!=null) {
			Set<SbbEntityID> output=new HashSet<SbbEntityID>();
			Iterator<SbbEntityID> keys=map.keySet().iterator();
			while(keys.hasNext()) {
				SbbEntityID curr=keys.next();
				if(curr.getParentChildRelation()!=null && curr.getParentChildRelation().equals(getChildRelationMethod))
					output.add(curr);
			}
			
			return output;
		}
		else
			return new HashSet<SbbEntityID>();
	}
}