/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileUpdatedEvent;

import org.mobicents.slee.container.profile.ProfileLocalObjectConcrete;
import org.mobicents.slee.runtime.activity.ActivityContext;

/**
 * Profile Updated Event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 *@author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class ProfileUpdatedEventImpl extends SuperProfileEvent implements ProfileUpdatedEvent {

	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileUpdatedEvent", "javax.slee", "1.0");

	private ProfileLocalObjectConcrete profileLocalObjectBeforeAction = null;

	public ProfileUpdatedEventImpl(Address profileAddress, ProfileID profile, ProfileLocalObjectConcrete profileLocalObjectBeforeAction, ProfileLocalObjectConcrete profileLocalObjectAfterAction,
			ActivityContext activityContext) {
		super(profileAddress, profile, profileLocalObjectAfterAction, activityContext);

		this.profileLocalObjectBeforeAction = profileLocalObjectBeforeAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getProfile()
	 */
	public ProfileID getProfile() {
		return super.profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getProfileAddress()
	 */
	public Address getProfileAddress() {

		return super.profileAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getBeforeUpdateProfile()
	 */
	public Object getBeforeUpdateProfile() {
		if (super.isClassLoaded(super.profileLocalObjectAfterAction.getClass())) {
			return this.profileLocalObjectBeforeAction.getProfileObject().getProfileConcrete();
		} else {
			return null;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getAfterUpdateProfile()
	 */
	public Object getAfterUpdateProfile() {
		if (super.isClassLoaded(super.profileLocalObjectAfterAction.getClass())) {
			return super.profileLocalObjectAfterAction.getProfileObject().getProfileConcrete();
		} else {
			return null;
		}
		
	}

	public EventTypeID getEventTypeID() {
		return EVENT_TYPE_ID;
	}

	public Object getEventObject() {
		// return ((ProfileTableActivityImpl)
		// this.activityContextInterface.getActivity()).getProfileEvent();
		return (ProfileUpdatedEvent) this;
	}

	public Address getAddress() {
		return super.profileAddress;
	}

	public ProfileLocalObject getAfterUpdateProfileLocal() {
		if (super.isClassLoaded(super.profileLocalObjectAfterAction.getClass())) {
			return super.profileLocalObjectAfterAction;
		} else {
			return null;
		}
	}

	public ProfileLocalObject getBeforeUpdateProfileLocal() {
		if (super.isClassLoaded(this.profileLocalObjectBeforeAction.getClass())) {
			return this.profileLocalObjectBeforeAction;
		} else {
			return null;
		}
	}

}
