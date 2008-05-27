package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to manage call leg event reports  was unsuccessful, and the reason (for example, the parameters were incorrect, the request was refused, etc.).
 * 
 * 
 */
public class EventReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for EventReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public EventReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier , org.csapi.cc.TpCallError errorIndication ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
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
     * Returns the tpCallLegIdentifier
     * 
     */
    public TpCallLegIdentifier getTpCallLegIdentifier() {
        return this.tpCallLegIdentifier;
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
        if(!(o instanceof EventReportErrEvent)) {
            return false;
        } 
        EventReportErrEvent eventReportErrEvent = (EventReportErrEvent) o;
        if(!(this.getService() == eventReportErrEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (eventReportErrEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(eventReportErrEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (eventReportErrEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(eventReportErrEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (eventReportErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(eventReportErrEvent.errorIndication)))  {
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
    private org.csapi.cc.TpCallError errorIndication = null;

} // EventReportErrEvent

