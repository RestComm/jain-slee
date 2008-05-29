package org.mobicents.csapi.jr.slee.cc.cccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event is called when a conference is created from an earlier resource reservation.  If the application has previously explicitly passed a reference to the callback using a setCallbackWithSessionID() invocation, this parameter may be null, or if supplied must be the same as that provided during the setCallbackWithSessionID().
@return appInterface : Specifies a reference to the application interface which implements the callback interface for the new conference.
 * 
 * 
 */
public class ConferenceCreatedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ConferenceCreatedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ConferenceCreatedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpConfCallIdentifier conferenceCall ){
        super(tpServiceIdentifier);
        this.conferenceCall = conferenceCall;
    }

    /**
     * Returns the conferenceCall
     * 
     */
    public TpConfCallIdentifier getConferenceCall() {
        return this.conferenceCall;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ConferenceCreatedEvent)) {
            return false;
        } 
        ConferenceCreatedEvent conferenceCreatedEvent = (ConferenceCreatedEvent) o;
        if(!(this.getService() == conferenceCreatedEvent.getService())) {
            return false;
        }
        if ((this.conferenceCall != null) && (conferenceCreatedEvent.conferenceCall != null)) {
            if(!(this.conferenceCall.equals(conferenceCreatedEvent.conferenceCall)))  {
                return false;
            }
        }
        if (this.hashCode() != o.hashCode()) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hashcode value for the object.
     */
    public int hashCode() {
        return 1;
    }

    // VARIABLES
    // .......................................................

    private TpConfCallIdentifier conferenceCall = null;

} // ConferenceCreatedEvent

