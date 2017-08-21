/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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