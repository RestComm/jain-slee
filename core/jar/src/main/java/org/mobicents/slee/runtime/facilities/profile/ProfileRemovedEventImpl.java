/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileAddedEventImpl.java
 * 
 * Created on Jan 27, 2005
 * 
 */
package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileRemovedEvent;

import org.mobicents.slee.container.profile.ProfileLocalObjectConcrete;
import org.mobicents.slee.runtime.activity.ActivityContext;

/**
 * Profile removed event implementation.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * 
 */
public class ProfileRemovedEventImpl extends SuperProfileEvent implements ProfileRemovedEvent {

	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileRemovedEvent", "javax.slee", "1.0");

	public ProfileRemovedEventImpl(Address profileAddress, ProfileID profile, ProfileLocalObjectConcrete profileLocalObject, ActivityContext activityContext) {
		super(profileAddress, profile, profileLocalObject, activityContext);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileAddedEvent#getProfile()
	 */
	public ProfileID getProfile() {

		return super.profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileAddedEvent#getProfileAddress()
	 */
	public Address getProfileAddress() {

		return super.profileAddress;
	}

	public EventTypeID getEventTypeID() {
		return EVENT_TYPE_ID;
	}

	public Object getEventObject() {
		// return ((ProfileTableActivityImpl)
		// this.activityContextInterface.getActivity()).getProfileEvent();
		return (ProfileRemovedEvent) this;
	}

	public Address getAddress() {
		return super.profileAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileRemovedEvent#getRemovedProfile()
	 */
	public Object getRemovedProfile() {
		if (super.isClassLoaded(super.profileLocalObjectAfterAction.getClass())) {
			return super.profileLocalObjectAfterAction.getProfileObject().getProfileConcrete();
		} else {
			return null;
		}
		
	}

	public ProfileLocalObject getRemovedProfileLocal() {
		if (super.isClassLoaded(super.profileLocalObjectAfterAction.getClass())) {
			return super.profileLocalObjectAfterAction;
		} else {
			return null;
		}
	}

}
