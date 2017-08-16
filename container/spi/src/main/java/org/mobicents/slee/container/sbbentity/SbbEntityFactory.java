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

	/**
	 * Orders the specified set of {@link SbbEntityID}, according to priority of
	 * related Sbb Entities. In case 2 Sbb Entities have same priority, order is
	 * defined by comparing the toString() of the IDs.
	 * 
	 * @param ids
	 * @return
	 */
	public Set<SbbEntityID> sortByPriority(Set<SbbEntityID> ids);
}
