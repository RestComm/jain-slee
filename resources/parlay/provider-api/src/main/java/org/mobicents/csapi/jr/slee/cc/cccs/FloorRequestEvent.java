package org.mobicents.csapi.jr.slee.cc.cccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event is used to inform the application about the floor requests from the network. The application can grant the request by calling the appointSpeaker method.
 * 
 * 
 */
public class FloorRequestEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for FloorRequestEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public FloorRequestEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpSubConfCallIdentifier tpSubConfCallIdentifier , int callLegSessionID ){
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
        if(!(o instanceof FloorRequestEvent)) {
            return false;
        } 
        FloorRequestEvent floorRequestEvent = (FloorRequestEvent) o;
        if(!(this.getService() == floorRequestEvent.getService())) {
            return false;
        }
        if ((this.tpSubConfCallIdentifier != null) && (floorRequestEvent.tpSubConfCallIdentifier != null)) {
            if(!(this.tpSubConfCallIdentifier.equals(floorRequestEvent.tpSubConfCallIdentifier)))  {
                return false;
            }
        }
        if(!(this.callLegSessionID == floorRequestEvent.callLegSessionID)) {
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

} // FloorRequestEvent

