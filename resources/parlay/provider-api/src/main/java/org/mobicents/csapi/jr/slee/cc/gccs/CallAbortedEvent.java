package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the call object (at the gateway) has aborted or terminated abnormally. No further communication will be possible between the call and application.
 * 
 * 
 */
public class CallAbortedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallAbortedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallAbortedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CallAbortedEvent)) {
            return false;
        } 
        CallAbortedEvent callAbortedEvent = (CallAbortedEvent) o;
        if(!(this.getService() == callAbortedEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (callAbortedEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(callAbortedEvent.tpCallIdentifier)))  {
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

    private TpCallIdentifier tpCallIdentifier = null;

} // CallAbortedEvent

