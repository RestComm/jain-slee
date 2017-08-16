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

package org.mobicents.slee.container.sbbentity;

import java.io.Externalizable;

import javax.slee.ServiceID;

/**
 * 
 * @author martins
 *
 */
public interface SbbEntityID extends Externalizable {

	/**
	 * Retrieves the parent's sbb entity identifier.
	 * @return null if root sbb entity.
	 */
	public SbbEntityID getParentSBBEntityID();
	
	/**
	 * Retrieves the parent's child relation.
	 * @return null if root sbb entity.
	 */
	public String getParentChildRelation();
	
	/**
	 * Retrieves the first element of a sbb entity ID, the identifier of its Service.
	 * @return
	 */
	public ServiceID getServiceID();
	
	/**
	 * Retrieves the second element of a sbb entity ID, the convergence name.
	 * @return
	 */
	public String getServiceConvergenceName();
	
	/**
	 * Indicates if the sbb entity identifier is related to a root sbb entity
	 * @return true if {@link SbbEntityID#getParentSBBEntityID()} is null
	 */
	public boolean isRootSbbEntity();
	
	/**
	 * Retrieves the related root sbb entity identifier.
	 * @return same id if root sbb entity.
	 */
	public SbbEntityID getRootSBBEntityID();
	
	/**
	 * Retrieves the sbb entity name.
	 * @return
	 */
	public String getName();
	
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object obj);
	
	@Override
	public String toString();
}
