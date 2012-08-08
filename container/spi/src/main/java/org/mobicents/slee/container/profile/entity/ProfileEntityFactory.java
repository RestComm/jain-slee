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

package org.mobicents.slee.container.profile.entity;

import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;

/**
 * Interface for a {@link ProfileEntity} factory object, which can be used to
 * build specific entity objects for an {@link ProfileSpecificationComponent}.
 * 
 * @author martins
 * 
 */
public interface ProfileEntityFactory {

	/**
	 * Creates a new {@link ProfileEntity} instance with the specified profile
	 * name, for the specified profile table name.
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return
	 */
	public ProfileEntity newInstance(String profileTableName, String profileName);
	
	/**
	 * Copies the attributes between two instances of {@link ProfileEntity}.
	 * 
	 * @param from
	 * @param to
	 */
	public void copyAttributes(ProfileEntity from, ProfileEntity to);
	
}
