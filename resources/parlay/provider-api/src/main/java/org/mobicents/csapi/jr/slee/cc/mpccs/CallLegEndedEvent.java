package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the leg has terminated in the network. The application has received all requested results (e.g,, getInfoRes) related to the call leg. The call leg will be destroyed after returning from this method.
 * 
 * 
 */
public class CallLegEndedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallLegEndedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallLegEndedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier , org.csapi.cc.TpReleaseCause cause ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.cause = cause;
    }

    /**
     * Returns the tpMultiPartyCallIdentifier
     * 
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return this.tpMultiPartyCallIdentifier;
    }
    /**
     * Returns the tpCallLegIdentifier
     * 
     */
    public TpCallLegIdentifier getTpCallLegIdentifier() {
        return this.tpCallLegIdentifier;
    }
    /**
     * Returns the cause
     * 
     */
    public org.csapi.cc.TpReleaseCause getCause() {
        return this.cause;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CallLegEndedEvent)) {
            return false;
        } 
        CallLegEndedEvent callLegEndedEvent = (CallLegEndedEvent) o;
        if(!(this.getService() == callLegEndedEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (callLegEndedEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(callLegEndedEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (callLegEndedEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(callLegEndedEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.cause != null) && (callLegEndedEvent.cause != null)) {
            if(!(this.cause.equals(callLegEndedEvent.cause)))  {
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
    private TpCallLegIdentifier tpCallLegIdentifier = null;
    private org.csapi.cc.TpReleaseCause cause = null;

} // CallLegEndedEvent

