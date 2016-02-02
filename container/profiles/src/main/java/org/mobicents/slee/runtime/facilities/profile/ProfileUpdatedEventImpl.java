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
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileUpdatedEvent;

import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.profile.entity.ProfileEntity;

/**
 * Profile Updated Event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski</a>
 * @author martins
 */
public class ProfileUpdatedEventImpl extends AbstractProfileEvent implements ProfileUpdatedEvent {

	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileUpdatedEvent", "javax.slee", "1.0");

	private final ProfileEntity profileBeforeAction;

	public ProfileUpdatedEventImpl(ProfileEntity profileBeforeAction, ProfileEntity profileAfterAction, ProfileManagementImpl profileManagement) {
		super(profileAfterAction,profileManagement);
		this.profileBeforeAction = profileBeforeAction;
	}

	@Override
	public EventTypeID getEventTypeID() {
		return EVENT_TYPE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getAfterUpdateProfile()
	 */
	public Object getAfterUpdateProfile() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileCmpSlee10Wrapper();
		} else {
			return null;
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getAfterUpdateProfileLocal()
	 */
	public ProfileLocalObject getAfterUpdateProfileLocal() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileLocalObject();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getBeforeUpdateProfile()
	 */
	public Object getBeforeUpdateProfile() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(this.profileBeforeAction).getProfileCmpSlee10Wrapper();
		} else {
			return null;
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getBeforeUpdateProfileLocal()
	 */
	public ProfileLocalObject getBeforeUpdateProfileLocal() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(this.profileBeforeAction).getProfileLocalObject();
		} else {
			return null;
		}
	}

}
