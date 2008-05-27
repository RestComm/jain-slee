package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the call has terminated in the network. 
Note that the event that caused the call to end might have been received separately if the application was monitoring for it.
 * 
 * 
 */
public class CallEndedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallEndedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallEndedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , org.csapi.cc.TpCallEndedReport report ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.report = report;
    }

    /**
     * Returns the tpMultiPartyCallIdentifier
     * 
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return this.tpMultiPartyCallIdentifier;
    }
    /**
     * Returns the report
     * 
     */
    public org.csapi.cc.TpCallEndedReport getReport() {
        return this.report;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CallEndedEvent)) {
            return false;
        } 
        CallEndedEvent callEndedEvent = (CallEndedEvent) o;
        if(!(this.getService() == callEndedEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (callEndedEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(callEndedEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.report != null) && (callEndedEvent.report != null)) {
            if(!(this.report.equals(callEndedEvent.report)))  {
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
    private org.csapi.cc.TpCallEndedReport report = null;

} // CallEndedEvent

