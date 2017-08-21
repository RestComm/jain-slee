package org.mobicents.slee.runtime.sbbentity.cache;

import java.util.HashMap;

import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.MobicentsCache;
import org.restcomm.cluster.cache.ClusteredCacheData;

public class ParentEntityCacheData extends ClusteredCacheData<SbbEntityCacheKey, HashMap<SbbEntityID, Void>> {
	private SbbEntityID sbbEntityID;

	public ParentEntityCacheData(SbbEntityID sbbEntityID, MobicentsCache cache) {
		super(new SbbEntityCacheKey(sbbEntityID.getParentSBBEntityID(), SbbEntityCacheType.CHILD_RELATIONS), cache);
		this.sbbEntityID = sbbEntityID;
	}

	public Boolean create(Boolean createIfNotExists) {
		if (sbbEntityID.isRootSbbEntity())
			return false;

		HashMap<SbbEntityID, Void> map = super.getValue();
		if (map == null && createIfNotExists)
			map = new HashMap<SbbEntityID, Void>();

		if (map != null) {
			if (map.containsKey(sbbEntityID)) {
				return false;
			} else {
				map = new HashMap<SbbEntityID, Void>(map);
				map.put(sbbEntityID, null);
				super.putValue(map);
				return true;
			}
		} else
			return false;
	}

	public Boolean removeNode() {
		if (sbbEntityID.isRootSbbEntity())
			return false;

		HashMap<SbbEntityID, Void> map = super.getValue();
		if (map != null) {
			if (!map.containsKey(sbbEntityID)) {
				return false;
			} else {
				map = new HashMap<SbbEntityID, Void>(map);
				map.remove(sbbEntityID);
				super.putValue(map);
				return true;
			}
		} else
			return false;
	}
}