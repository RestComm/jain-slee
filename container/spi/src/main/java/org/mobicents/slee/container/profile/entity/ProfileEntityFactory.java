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
