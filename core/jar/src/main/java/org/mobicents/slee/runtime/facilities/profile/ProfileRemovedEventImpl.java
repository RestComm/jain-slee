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
import javax.slee.profile.ProfileRemovedEvent;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/** Profile removed event implementation.
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a> 
 *
 */
public class ProfileRemovedEventImpl implements ProfileRemovedEvent {
    
	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileRemovedEvent","javax.slee","1.0");
	    
    private Address profileAddress;
    
    private ProfileID profile;
    
    private Object  removedProfile;
    
    public ProfileRemovedEventImpl ( Address profileAddress, ProfileID profileId, 
            Object removedProfile, ProfileTableActivityContextInterfaceFactoryImpl ptableAcif ) {
        this.profileAddress = profileAddress;
        this.profile = profileId;
        this.removedProfile = removedProfile;        
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

    public EventTypeID getEventTypeID() {
        return EVENT_TYPE_ID;
    }

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

}

