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
