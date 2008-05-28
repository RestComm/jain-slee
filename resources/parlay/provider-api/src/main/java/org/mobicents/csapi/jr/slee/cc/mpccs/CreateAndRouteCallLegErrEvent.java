package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to route the call leg to the destination party was unsuccessful - the call leg could not be routed to the destination party (for example, the network was unable to route the call leg, the parameters were incorrect, the request was refused, etc.). Note that the event cases that can be monitored and correspond to an unsuccessful setup of a connection (e.g. busy, no_answer) will be reported by eventReportRes() and not by this operation.
 * 
 * 
 */
public class CreateAndRouteCallLegErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CreateAndRouteCallLegErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CreateAndRouteCallLegErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier callLegReference , org.csapi.cc.TpCallError errorIndication ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.callLegReference = callLegReference;
        this.errorIndication = errorIndication;
    }

    /**
     * Returns the tpMultiPartyCallIdentifier
     * 
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return this.tpMultiPartyCallIdentifier;
    }
    /**
     * Returns the callLegReference
     * 
     */
    public TpCallLegIdentifier getCallLegReference() {
        return this.callLegReference;
    }
    /**
     * Returns the errorIndication
     * 
     */
    public org.csapi.cc.TpCallError getErrorIndication() {
        return this.errorIndication;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CreateAndRouteCallLegErrEvent)) {
            return false;
        } 
        CreateAndRouteCallLegErrEvent createAndRouteCallLegErrEvent = (CreateAndRouteCallLegErrEvent) o;
        if(!(this.getService() == createAndRouteCallLegErrEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (createAndRouteCallLegErrEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(createAndRouteCallLegErrEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.callLegReference != null) && (createAndRouteCallLegErrEvent.callLegReference != null)) {
            if(!(this.callLegReference.equals(createAndRouteCallLegErrEvent.callLegReference)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (createAndRouteCallLegErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(createAndRouteCallLegErrEvent.errorIndication)))  {
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
    private TpCallLegIdentifier callLegReference = null;
    private org.csapi.cc.TpCallError errorIndication = null;

} // CreateAndRouteCallLegErrEvent

