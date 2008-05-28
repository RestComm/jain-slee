package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports that the original request was erroneous, or resulted in an error condition.
 * 
 * 
 */
public class DetachMediaErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DetachMediaErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DetachMediaErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier , org.csapi.cc.TpCallError errorIndication ){
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
        if(!(o instanceof DetachMediaErrEvent)) {
            return false;
        } 
        DetachMediaErrEvent detachMediaErrEvent = (DetachMediaErrEvent) o;
        if(!(this.getService() == detachMediaErrEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (detachMediaErrEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(detachMediaErrEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (detachMediaErrEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(detachMediaErrEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (detachMediaErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(detachMediaErrEvent.errorIndication)))  {
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

} // DetachMediaErrEvent

