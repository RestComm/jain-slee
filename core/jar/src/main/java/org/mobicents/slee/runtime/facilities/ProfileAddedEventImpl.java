/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

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
import javax.slee.profile.ProfileAddedEvent;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;

/** Profile added event implementation.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 *
 */
public class ProfileAddedEventImpl implements ProfileAddedEvent, SleeEvent {
    
	private static EventTypeIDImpl eventTypeID;
	private static EventTypeID lookupEventTypeID() {
		if (eventTypeID == null) {
			eventTypeID = SleeContainer.lookupFromJndi().getEventManagement().getEventType(new ComponentKey(
					"javax.slee.profile.ProfileAddedEvent","javax.slee","1.0"));
		}
		return eventTypeID;
	}
    
    private ActivityContextInterface aci;
    
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
     * @see javax.slee.profile.ProfileAddedEvent#getAddedProfile()
     */
    public Object getAddedProfile() {
       
        return this.addedProfile;
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
        return  (ProfileAddedEvent) this;
    }

    public Address getAddress() {
        return profileAddress;
    }

    public String getActivityContextID() {
        // TODO Auto-generated method stub
        return this.activityContextInterface.getActivityContext().getActivityContextId();
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

