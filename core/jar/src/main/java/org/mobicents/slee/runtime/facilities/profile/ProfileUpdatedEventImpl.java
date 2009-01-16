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
import javax.slee.profile.ProfileUpdatedEvent;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/**
 * Profile Updated Event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 *
 */
public class ProfileUpdatedEventImpl implements ProfileUpdatedEvent {
    
	private static EventTypeIDImpl eventTypeID;
	private static EventTypeID lookupEventTypeID() {
		if (eventTypeID == null) {
			eventTypeID = SleeContainer.lookupFromJndi().getEventManagement().getEventType(new ComponentKey(
					"javax.slee.profile.ProfileUpdatedEvent","javax.slee","1.0"));
		}
		return eventTypeID;
	}
	
    private ProfileID profile;
    private Address profileAddress;
    private Object beforeUpdateProfile;
    private Object afterUpdateProfile;
    private ActivityContextInterfaceImpl activityContextInterface;
      
    public ProfileUpdatedEventImpl( Address profileAddress,ProfileID profile,                          
            Object beforeUpdateProfile, Object afterUpdateProfile,
            ActivityContextInterfaceImpl activityContext,
            ProfileTableActivityContextInterfaceFactoryImpl  pACIFactory ) {
        this.profile = profile;
        this.profileAddress = profileAddress;
        this.beforeUpdateProfile = beforeUpdateProfile;
        this.afterUpdateProfile = afterUpdateProfile;
        this.activityContextInterface = activityContext;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileUpdatedEvent#getProfile()
     */
    public ProfileID getProfile() {
        return this.profile;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileUpdatedEvent#getProfileAddress()
     */
    public Address getProfileAddress() {
    
        return this.profileAddress;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileUpdatedEvent#getBeforeUpdateProfile()
     */
    public Object getBeforeUpdateProfile() {
     
        return this.beforeUpdateProfile;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileUpdatedEvent#getAfterUpdateProfile()
     */
    public Object getAfterUpdateProfile() {
        // TODO Auto-generated method stub
        return this.afterUpdateProfile;
    }

    public EventTypeID getEventTypeID() {
        return lookupEventTypeID();
    }

    public Object getEventObject() {
        //return  ((ProfileTableActivityImpl) this.activityContextInterface.getActivity()).getProfileEvent();
        return  (ProfileUpdatedEvent) this;
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

}

