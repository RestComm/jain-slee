package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports all the necessary information requested by the application, for example to calculate charging.
 * 
 * 
 */
public class GetInfoResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for GetInfoResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public GetInfoResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , org.csapi.cc.TpCallInfoReport callInfoReport ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.callInfoReport = callInfoReport;
    }


    /**
     * Constructor for GetInfoResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public GetInfoResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier , org.csapi.cc.TpCallLegInfoReport callLegInfoReport ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.callLegInfoReport = callLegInfoReport;
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
     * Returns the callInfoReport
     * 
     */
    public org.csapi.cc.TpCallInfoReport getCallInfoReport() {
        return this.callInfoReport;
    }
    /**
     * Returns the callLegInfoReport
     * 
     */
    public org.csapi.cc.TpCallLegInfoReport getCallLegInfoReport() {
        return this.callLegInfoReport;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof GetInfoResEvent)) {
            return false;
        } 
        GetInfoResEvent getInfoResEvent = (GetInfoResEvent) o;
        if(!(this.getService() == getInfoResEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (getInfoResEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(getInfoResEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (getInfoResEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(getInfoResEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.callInfoReport != null) && (getInfoResEvent.callInfoReport != null)) {
            if(!(this.callInfoReport.equals(getInfoResEvent.callInfoReport)))  {
                return false;
            }
        }
        if ((this.callLegInfoReport != null) && (getInfoResEvent.callLegInfoReport != null)) {
            if(!(this.callLegInfoReport.equals(getInfoResEvent.callLegInfoReport)))  {
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
    private org.csapi.cc.TpCallInfoReport callInfoReport = null;
    private org.csapi.cc.TpCallLegInfoReport callLegInfoReport = null;

} // GetInfoResEvent

