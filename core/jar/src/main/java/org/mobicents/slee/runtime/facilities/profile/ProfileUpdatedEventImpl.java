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

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.profile.ProfileLocalObjectConcrete;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

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

	private Object profileCmpInterfaceConcreteBeforeAction;
	private ProfileLocalObject profileLocalObjectBeforeAction = null;

	public ProfileUpdatedEventImpl(Address profileAddress, ProfileID profile, ProfileTableActivityContextInterfaceFactoryImpl ptableAcif, ActivityContextInterfaceImpl activityContextInterface,
			ProfileLocalObject profileLocalObjectBeforeAction, ProfileLocalObjectConcrete profileLocalObjectAfterAction, Object profileCmpInterfaceConcreteBeforeAction,
			Object profileCmpInterfaceConcreteAfterAction) {
		super(profileAddress, profile, profileCmpInterfaceConcreteAfterAction, ptableAcif, activityContextInterface, profileLocalObjectAfterAction);
		this.profileCmpInterfaceConcreteBeforeAction = profileCmpInterfaceConcreteBeforeAction;
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

		return this.profileCmpInterfaceConcreteBeforeAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileUpdatedEvent#getAfterUpdateProfile()
	 */
	public Object getAfterUpdateProfile() {
		// TODO Auto-generated method stub
		return super.profileCmpInterfaceConcreteAfterAction;
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

	public ActivityContextInterfaceImpl getActivityContextInterface() {
		return super.activityContextInterface;
	}

	public void setActivityContextInterfaceImpl(ActivityContextInterfaceImpl activityContextInterface) {
		super.activityContextInterface = activityContextInterface;
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
