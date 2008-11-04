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
package org.mobicents.slee.runtime.facilities;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.SleeEvent;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileRemovedEvent;

/** Profile removed event implementation.
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a> 
 *
 */
public class ProfileRemovedEventImpl implements ProfileRemovedEvent, SleeEvent {
    
	private static EventTypeIDImpl eventTypeID;
	private static EventTypeID lookupEventTypeID() {
		if (eventTypeID == null) {
			eventTypeID = SleeContainer.lookupFromJndi().getEventManagement().getEventType(new ComponentKey(
					"javax.slee.profile.ProfileRemovedEvent","javax.slee","1.0"));
		}
		return eventTypeID;
	}
	
    private ActivityContextInterface aci;
    
    private Address profileAddress;
    
    private ProfileID profile;
    
    private Object  removedProfile;
    
    private ProfileTableActivityContextInterfaceFactoryImpl ptableAcif;
    
    private ActivityContextInterfaceImpl activityContextInterface;
    
    public ProfileRemovedEventImpl ( Address profileAddress, ProfileID profileId, 
            Object removedProfile, ActivityContextInterfaceImpl acii,
            ProfileTableActivityContextInterfaceFactoryImpl ptableAcif ) {
        this.profileAddress = profileAddress;
        this.profile = profileId;
        this.removedProfile = removedProfile;
        this.activityContextInterface = acii;
    }
    
    public Object getActivity() {
        return this.aci.getActivity();
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileAddedEvent#getProfile()
     */
    public ProfileID getProfile() {
       
        return profile;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileAddedEvent#getProfileAddress()
     */
    public Address getProfileAddress() {
       
        return profileAddress;
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
        return lookupEventTypeID();
    }

    
    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#getEventObject()
     */
    public Object getEventObject() {
        //return  ((ProfileTableActivityImpl) this.activityContextInterface.getActivity()).getProfileEvent();
        return  (ProfileRemovedEvent) this;
    }

    public Address getAddress() {
        return this.profileAddress;
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileRemovedEvent#getRemovedProfile()
     */
    public Object getRemovedProfile() {
        return this.removedProfile;
    }

    public String getActivityContextID() {
        // TODO Auto-generated method stub
        return getActivityContextID();
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

