/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.facilities.cluster;

import javax.slee.facilities.NameAlreadyBoundException;
import javax.slee.facilities.NameNotBoundException;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.CacheData;
import org.restcomm.cluster.MobicentsCluster;

/**
 * 
 * Proxy object for ac naming facility data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextNamingFacilityCacheData extends CacheData<String, ActivityContextHandle> {

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

		super.put(ach);
	}

	/**
	 * Unbinds the specified aci name with the specified activity context id
	 * 
	 * @param name
	 * @return
	 * @throws NameNotBoundException
	 */
	public ActivityContextHandle unbindName() throws NameNotBoundException {
		ActivityContextHandle oldValue = super.remove();
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
		return super.get();
	}
}
