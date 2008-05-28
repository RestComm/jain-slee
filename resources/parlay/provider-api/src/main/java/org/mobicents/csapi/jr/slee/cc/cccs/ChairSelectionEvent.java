package org.mobicents.csapi.jr.slee.cc.cccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event is used to inform the application about the chair selection requests from the network. The application can grant the request by calling the chairSelection method on the subconference.
 * 
 * 
 */
public class ChairSelectionEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ChairSelectionEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ChairSelectionEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpSubConfCallIdentifier tpSubConfCallIdentifier , int callLegSessionID ){
        super(tpServiceIdentifier);
        this.tpSubConfCallIdentifier = tpSubConfCallIdentifier;
        this.callLegSessionID = callLegSessionID;
    }

    /**
     * Returns the tpSubConfCallIdentifier
     * 
     */
    public TpSubConfCallIdentifier getTpSubConfCallIdentifier() {
        return this.tpSubConfCallIdentifier;
    }
    /**
     * Returns the callLegSessionID
     * 
     */
    public int getCallLegSessionID() {
        return this.callLegSessionID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ChairSelectionEvent)) {
            return false;
        } 
        ChairSelectionEvent chairSelectionEvent = (ChairSelectionEvent) o;
        if(!(this.getService() == chairSelectionEvent.getService())) {
            return false;
        }
        if ((this.tpSubConfCallIdentifier != null) && (chairSelectionEvent.tpSubConfCallIdentifier != null)) {
            if(!(this.tpSubConfCallIdentifier.equals(chairSelectionEvent.tpSubConfCallIdentifier)))  {
                return false;
            }
        }
        if(!(this.callLegSessionID == chairSelectionEvent.callLegSessionID)) {
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

    private TpSubConfCallIdentifier tpSubConfCallIdentifier = null;
    private int callLegSessionID;

} // ChairSelectionEvent

