package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the call object has aborted or terminated abnormally. No further communication will be possible between the call and application.
 * 
 * 
 */
public class CallAbortedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallAbortedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallAbortedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
    }

    /**
     * Returns the tpMultiPartyCallIdentifier
     * 
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return this.tpMultiPartyCallIdentifier;
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
        if ((this.tpMultiPartyCallIdentifier != null) && (callAbortedEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(callAbortedEvent.tpMultiPartyCallIdentifier)))  {
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

    private TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier = null;

} // CallAbortedEvent

