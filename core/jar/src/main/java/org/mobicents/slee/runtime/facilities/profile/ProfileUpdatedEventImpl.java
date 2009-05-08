/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.EventTypeID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileUpdatedEvent;

import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;

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

	public ProfileUpdatedEventImpl(ProfileEntity profileBeforeAction, ProfileEntity profileAfterAction) {
		super(profileAfterAction);
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
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileConcrete();
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
			return getProfileObjectValidInCurrentTransaction(this.profileBeforeAction).getProfileConcrete();
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
