package org.mobicents.csapi.jr.slee.cc.cccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that a party (leg) has left  the conference.
 * 
 * 
 */
public class LeaveMonitorResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for LeaveMonitorResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public LeaveMonitorResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpConfCallIdentifier tpConfCallIdentifier , TpSubConfCallIdentifier tpSubConfCallIdentifier ){
        super(tpServiceIdentifier);
        this.tpConfCallIdentifier = tpConfCallIdentifier;
        this.tpSubConfCallIdentifier = tpSubConfCallIdentifier;
    }

    /**
     * Returns the tpConfCallIdentifier
     * 
     */
    public TpConfCallIdentifier getTpConfCallIdentifier() {
        return this.tpConfCallIdentifier;
    }
    /**
     * Returns the tpSubConfCallIdentifier
     * 
     */
    public TpSubConfCallIdentifier getTpSubConfCallIdentifier() {
        return this.tpSubConfCallIdentifier;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof LeaveMonitorResEvent)) {
            return false;
        } 
        LeaveMonitorResEvent leaveMonitorResEvent = (LeaveMonitorResEvent) o;
        if(!(this.getService() == leaveMonitorResEvent.getService())) {
            return false;
        }
        if ((this.tpConfCallIdentifier != null) && (leaveMonitorResEvent.tpConfCallIdentifier != null)) {
            if(!(this.tpConfCallIdentifier.equals(leaveMonitorResEvent.tpConfCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpSubConfCallIdentifier != null) && (leaveMonitorResEvent.tpSubConfCallIdentifier != null)) {
            if(!(this.tpSubConfCallIdentifier.equals(leaveMonitorResEvent.tpSubConfCallIdentifier)))  {
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

    private TpConfCallIdentifier tpConfCallIdentifier = null;
    private TpSubConfCallIdentifier tpSubConfCallIdentifier = null;

} // LeaveMonitorResEvent

