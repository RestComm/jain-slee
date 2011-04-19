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

/**
 * 
 */
package org.mobicents.slee.container.sbbentity;

import java.util.Set;

import javax.slee.CreateException;
import javax.slee.ServiceID;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 * 
 */
public interface SbbEntityFactory extends SleeContainerModule {

	/**
	 * Creates a new root sbb entity.
	 * 
	 * @param serviceID
	 * @param convergenceName
	 * @return
	 */
	public SbbEntity createRootSbbEntity(ServiceID serviceID,
			String convergenceName);

	/**
	 * Creates a new non root sbb entity, if it does not exists. 
	 * 
	 * @param parentSbbEntityID
	 * @param parentChildRelation
	 * @param childName
	 * @return
	 * @throws CreateException 
	 */
	public SbbEntity createNonRootSbbEntity(SbbEntityID parentSbbEntityID,
			String parentChildRelation, String childName) throws CreateException;

	/**
	 * Retrieves the sbb entity with the specified id.
	 * 
	 * @param sbbEntityID
	 * @param lock
	 *            indicates if the sbb entity should be locked or not, to ensure
	 *            serial access.
	 * @return
	 */
	public SbbEntity getSbbEntity(SbbEntityID sbbEntityID, boolean lock);

	/**
	 * 
	 * @param serviceID
	 * @return
	 */
	public Set<SbbEntityID> getRootSbbEntityIDs(ServiceID serviceID);
	
	/**
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntityIDs();

	/**
	 * Removes the specified sbb entity.
	 * 
	 * @param sbbEntity
	 *            the sbb entity to remove
	 * @param useCurrentClassLoader
	 *            if false the factory will switch to the sbb's class loader
	 *            before removing the entity.
	 */
	public void removeSbbEntity(SbbEntity sbbEntity,
			boolean useCurrentClassLoader);

}
