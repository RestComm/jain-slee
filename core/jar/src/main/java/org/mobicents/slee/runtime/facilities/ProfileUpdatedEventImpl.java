/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/


package org.mobicents.slee.runtime.facilities;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileUpdatedEvent;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.SleeEvent;

/**
 * Profile Updated Event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 *
 */
public class ProfileUpdatedEventImpl implements ProfileUpdatedEvent,SleeEvent {
    
    private ActivityContextInterface aci;
    private ProfileID profile;
    private Address profileAddress;
    private Object beforeUpdateProfile;
    private Object afterUpdateProfile;
    private ActivityContextInterfaceImpl activityContextInterface;
    private EventTypeIDImpl eventTypeId;
    private SleeContainer  serviceContainer;
    private ProfileTableActivityContextInterfaceFactoryImpl profileActivityContextInterfaceFactory;
    
    public ProfileUpdatedEventImpl( Address profileAddress,ProfileID profile,                          
            Object beforeUpdateProfile, Object afterUpdateProfile,
            ActivityContextInterfaceImpl activityContext,
            ProfileTableActivityContextInterfaceFactoryImpl  pACIFactory ) {
        this.profile = profile;
        this.profileAddress = profileAddress;
        this.beforeUpdateProfile = beforeUpdateProfile;
        this.afterUpdateProfile = afterUpdateProfile;
        this.activityContextInterface = activityContext;
        this.profileActivityContextInterfaceFactory = pACIFactory;
        this.serviceContainer = SleeContainer.lookupFromJndi();
        this.eventTypeId = (EventTypeIDImpl) this.serviceContainer.getEventType
		(new ComponentKey("javax.slee.profile.ProfileUpdatedEvent","javax.slee","1.0"));
        
    }
    
    public Object getActivity() {
        return this.aci.getActivity();
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

    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#getActivityContext()
     */
    public ActivityContext getActivityContext() {
        return this.activityContextInterface.getActivityContext();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#getEventTypeID()
     */
    public EventTypeID getEventTypeID() {
        return this.eventTypeId;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#getEventObject()
     */
    public Object getEventObject() {
        //return  ((ProfileTableActivityImpl) this.activityContextInterface.getActivity()).getProfileEvent();
        return  (ProfileUpdatedEvent) this;
    }

    public Address getAddress() { 
        return profileAddress;
    }

    public String getActivityContextID() {
        return getActivityContext().getActivityContextId();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#getActivityContextInterface()
     */
    public ActivityContextInterface getActivityContextInterface() {
     
        return this.aci;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#setActivityContextInterface(javax.slee.ActivityContextInterface)
     */
    public void setActivityContextInterface(ActivityContextInterface aci) {
        this.aci = aci;
        
    }

}

