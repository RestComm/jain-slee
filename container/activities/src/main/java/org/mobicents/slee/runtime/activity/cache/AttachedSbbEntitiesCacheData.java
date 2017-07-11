package org.mobicents.slee.runtime.activity.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.MobicentsCache;

public class AttachedSbbEntitiesCacheData extends CacheData<ActivityCacheKey, HashMap<SbbEntityID, Void>> {
	public AttachedSbbEntitiesCacheData(ActivityContextHandle handle, MobicentsCache cache) {
		super(new ActivityCacheKey(handle, ActivityCacheType.ATTACHED_SBBS), cache);
	}

	public Boolean attachSbb(Boolean createIfNotExists, SbbEntityID sbbEntityID) {
		HashMap<SbbEntityID, Void> map = super.get();
		if (map == null && createIfNotExists)
			map = new HashMap<SbbEntityID, Void>();

		if (map != null) {
			if (map.containsKey(sbbEntityID)) {
				return false;
			} else {
				map = new HashMap<SbbEntityID, Void>(map);
				map.put(sbbEntityID, null);
				super.put(map);
				return true;
			}
		} else
			return false;
	}

	public Boolean detachSbb(Boolean createIfNotExists, SbbEntityID sbbEntityID) {
		HashMap<SbbEntityID, Void> map = super.get();
		if (map == null && createIfNotExists) {
			map = new HashMap<SbbEntityID, Void>();
			super.put(map);
			return false;
		}

		if (map != null) {
			if (!map.containsKey(sbbEntityID))
				return false;
			else {
				map = new HashMap<SbbEntityID, Void>(map);
				map.remove(sbbEntityID);
				super.put(map);
				return true;
			}
		} else
			return false;
	}

	public Boolean hasSbbs(Boolean createIfNotExists) {
		HashMap<SbbEntityID, Void> map = super.get();
		if (map == null && createIfNotExists) {
			map = new HashMap<SbbEntityID, Void>();
			super.put(map);
			return false;
		}

		if (map != null) {
			return map.size() != 0;
		}

		return false;
	}

	public Set<SbbEntityID> getSbbs(Boolean createIfNotExists) {
		HashMap<SbbEntityID, Void> map = super.get();
		if (map == null && createIfNotExists) {
			map = new HashMap<SbbEntityID, Void>();
			super.put(map);
			return map.keySet();
		}

		if (map != null) {
			return map.keySet();
		}

		return new HashSet<SbbEntityID>();
	}

	public void removeNode() {
		super.remove();
	}
}