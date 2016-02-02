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

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.EventTypeID;
import javax.slee.profile.ProfileAddedEvent;
import javax.slee.profile.ProfileLocalObject;

import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.profile.entity.ProfileEntity;

/**
 * Profile added event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski</a>
 * @author martins
 * 
 */
public class ProfileAddedEventImpl extends AbstractProfileEvent implements ProfileAddedEvent {

	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileAddedEvent", "javax.slee", "1.0");

	public ProfileAddedEventImpl(ProfileEntity profileEntity, ProfileManagementImpl profileManagement) {
		super(profileEntity,profileManagement);
	}

	@Override
	public EventTypeID getEventTypeID() {
		return EVENT_TYPE_ID;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileAddedEvent#getAddedProfile()
	 */
	public Object getAddedProfile() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileCmpSlee10Wrapper();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileAddedEvent#getAddedProfileLocal()
	 */
	public ProfileLocalObject getAddedProfileLocal() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileLocalObject();
		} else {
			return null;
		}
	}
}
