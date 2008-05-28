package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the call has terminated in the network. However, the application may still receive some results (e.g., getCallInfoRes) related to the call. The application is expected to deassign the call object after having received the callEnded.
Note that the event that caused the call to end might also be received separately if the application was monitoring for it.
 * 
 * 
 */
public class CallEndedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallEndedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallEndedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.gccs.TpCallEndedReport report ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.report = report;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }
    /**
     * Returns the report
     * 
     */
    public org.csapi.cc.gccs.TpCallEndedReport getReport() {
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
        if ((this.tpCallIdentifier != null) && (callEndedEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(callEndedEvent.tpCallIdentifier)))  {
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

    private TpCallIdentifier tpCallIdentifier = null;
    private org.csapi.cc.gccs.TpCallEndedReport report = null;

} // CallEndedEvent

