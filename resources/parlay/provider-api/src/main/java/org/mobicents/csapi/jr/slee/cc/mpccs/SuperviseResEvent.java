package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a call leg supervision event to the application when it has indicated its interest in this kind of event.
It is also called when the connection to a party  is terminated before the supervision event occurs.
 * 
 * 
 */
public class SuperviseResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier , int report , int usedTime ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.report = report;
        this.usedTime = usedTime;
    }


    /**
     * Constructor for SuperviseResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , int report , int usedTime ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.report = report;
        this.usedTime = usedTime;
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
     * Returns the report
     * 
     */
    public int getReport() {
        return this.report;
    }
    /**
     * Returns the usedTime
     * 
     */
    public int getUsedTime() {
        return this.usedTime;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SuperviseResEvent)) {
            return false;
        } 
        SuperviseResEvent superviseResEvent = (SuperviseResEvent) o;
        if(!(this.getService() == superviseResEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (superviseResEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(superviseResEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (superviseResEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(superviseResEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if(!(this.report == superviseResEvent.report)) {
            return false;
        }
        if(!(this.usedTime == superviseResEvent.usedTime)) {
            return false;
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
    private int report;
    private int usedTime;

} // SuperviseResEvent

