/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.profile.ProfileLocalObjectConcrete;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileAddedEvent;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;

/**
 * Profile added event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * 
 */
public class ProfileAddedEventImpl extends SuperProfileEvent implements ProfileAddedEvent {

	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileAddedEvent", "javax.slee", "1.0");

	public ProfileAddedEventImpl(Address profileAddress, ProfileID profile, Object profileCmpInterfaceConcrete, ProfileTableActivityContextInterfaceFactoryImpl ptableAcif,
			ActivityContextInterfaceImpl activityContextInterface, ProfileLocalObjectConcrete profileLocalObject) {
		super(profileAddress, profile, profileCmpInterfaceConcrete, ptableAcif, activityContextInterface, profileLocalObject);

		super.profileLocalObjectAfterAction.setSnapshot();
	}

	public Object getActivity() {
		return super.activityContextInterface.getActivity();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileAddedEvent#getAddedProfile()
	 */
	public Object getAddedProfile() {

		return super.profileCmpInterfaceConcreteAfterAction;
	}

	public EventTypeID getEventTypeID() {
		return EVENT_TYPE_ID;
	}

	public Object getEventObject() {
		// return ((ProfileTableActivityImpl)
		// this.activityContextInterface.getActivity()).getProfileEvent();
		return (ProfileAddedEvent) this;
	}

	public Address getAddress() {
		return profileAddress;
	}

	public ActivityContextInterfaceImpl getActivityContextInterface() {
		return this.activityContextInterface;
	}

	public void setActivityContextInterfaceImpl(ActivityContextInterfaceImpl activityContextInterface) {
		this.activityContextInterface = activityContextInterface;
	}

	public ProfileLocalObject getAddedProfileLocal() {
		if (super.isClassLoaded(super.profileLocalObjectAfterAction.getClass())) {
			return super.profileLocalObjectAfterAction;
		} else {
			return null;
		}

	}
}
