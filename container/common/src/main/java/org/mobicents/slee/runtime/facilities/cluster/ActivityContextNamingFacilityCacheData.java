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

package org.mobicents.slee.runtime.facilities.cluster;

import javax.slee.facilities.NameAlreadyBoundException;
import javax.slee.facilities.NameNotBoundException;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cluster.MobicentsCluster;
import org.restcomm.cluster.cache.ClusteredCacheData;

/**
 * 
 * Proxy object for ac naming facility data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextNamingFacilityCacheData extends ClusteredCacheData<String, ActivityContextHandle> {

	/**
	 * 
	 * @param cluster
	 */
	public ActivityContextNamingFacilityCacheData(String name, MobicentsCluster cluster) {
		super(name, cluster.getMobicentsCache());
	}

	/**
	 * Binds the specified aci name with the specified activity context handle
	 * 
	 * @param ach
	 * @param name
	 * @throws NameAlreadyBoundException
	 */
	public void bindName(ActivityContextHandle ach) throws NameAlreadyBoundException {
		if (super.exists())
			throw new NameAlreadyBoundException("name already bound");

		super.putValue(ach);
	}

	/**
	 * Unbinds the specified aci name with the specified activity context id
	 * 
	 * @param name
	 * @return
	 * @throws NameNotBoundException
	 */
	public ActivityContextHandle unbindName() throws NameNotBoundException {
		ActivityContextHandle oldValue = super.removeElement();
		if (oldValue == null)
			throw new NameNotBoundException("name not bound");

		return oldValue;
	}

	/**
	 * Lookup of the activity context id bound to the specified aci name
	 * 
	 * @param name
	 * @return
	 */
	public ActivityContextHandle lookupName() {
		return super.getValue();
	}
}
