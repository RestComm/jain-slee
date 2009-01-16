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
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileAddedEvent;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;

/** Profile added event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 *
 */
public class ProfileAddedEventImpl implements ProfileAddedEvent {
    
	private static EventTypeIDImpl eventTypeID;
	private static EventTypeID lookupEventTypeID() {
		if (eventTypeID == null) {
			eventTypeID = SleeContainer.lookupFromJndi().getEventManagement().getEventType(new ComponentKey(
					"javax.slee.profile.ProfileAddedEvent","javax.slee","1.0"));
		}
		return eventTypeID;
	}
    
    private Address profileAddress;
    
    private ProfileID profile;
    
    private Object  addedProfile;
    
    private ProfileTableActivityContextInterfaceFactoryImpl ptableAcif;
    
    private ActivityContextInterfaceImpl activityContextInterface;
    
    public ProfileAddedEventImpl ( Address profileAddress, ProfileID profileId, 
            Object addedProfile, ActivityContextInterface acii,
            ProfileTableActivityContextInterfaceFactory ptableAcif ) {
        this.profileAddress = profileAddress;
        this.profile = profileId;
        this.addedProfile = addedProfile;
        this.activityContextInterface = (ActivityContextInterfaceImpl)acii;
    }
    
    public Object getActivity() {
        return this.activityContextInterface.getActivity();
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
     * @see javax.slee.profile.ProfileAddedEvent#getAddedProfile()
     */
    public Object getAddedProfile() {
       
        return this.addedProfile;
    }

    public EventTypeID getEventTypeID() {
        return lookupEventTypeID();
    }

    public Object getEventObject() {
        //return  ((ProfileTableActivityImpl) this.activityContextInterface.getActivity()).getProfileEvent();
        return  (ProfileAddedEvent) this;
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

