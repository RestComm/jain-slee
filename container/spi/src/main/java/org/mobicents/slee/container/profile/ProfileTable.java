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
package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.slee.SLEEException;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedAttributeException;

import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.facilities.NotificationSourceWrapper;

/**
 * @author martins
 *
 */
public interface ProfileTable extends javax.slee.profile.ProfileTable {

	public Collection<ProfileID> getProfilesByAttribute(
			String attributeName, Object attributeValue, boolean isSlee11)
			throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException;
			
	public NotificationSourceWrapper getProfileTableNotification();

	/**
	 * @param profileName
	 * @return
	 */
	public boolean profileExists(String profileName);

	/**
	 * @param profileName
	 * @return
	 */
	public ProfileObject getProfile(String profileName);
	
	public ProfileSpecificationComponent getProfileSpecificationComponent();

}
