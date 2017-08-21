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
