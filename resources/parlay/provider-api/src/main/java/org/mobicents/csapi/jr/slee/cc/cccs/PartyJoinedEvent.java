package org.mobicents.csapi.jr.slee.cc.cccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that a new party (leg) has joined the conference. This can be used in, e.g., a meetme conference where the participants dial in to the conference using the address returned during reservation of the conference.
The Leg will be assigned to the default subconference object and will be in a detached state. The application may move the call Leg to a different subconference before attaching the media.
The method will only be called when joinAllowed is indicated in the conference policy. 
@return appCallLeg : Specifies the call back interface that should be used for callbacks from the new call Leg.
 * 
 * 
 */
public class PartyJoinedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for PartyJoinedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public PartyJoinedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpConfCallIdentifier tpConfCallIdentifier , org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier tpCallLegIdentifier , org.csapi.cc.cccs.TpJoinEventInfo eventInfo ){
        super(tpServiceIdentifier);
        this.tpConfCallIdentifier = tpConfCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.eventInfo = eventInfo;
    }

    /**
     * Returns the tpConfCallIdentifier
     * 
     */
    public TpConfCallIdentifier getTpConfCallIdentifier() {
        return this.tpConfCallIdentifier;
    }
    /**
     * Returns the tpCallLegIdentifier
     * 
     */
    public org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier getTpCallLegIdentifier() {
        return this.tpCallLegIdentifier;
    }
    /**
     * Returns the eventInfo
     * 
     */
    public org.csapi.cc.cccs.TpJoinEventInfo getEventInfo() {
        return this.eventInfo;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof PartyJoinedEvent)) {
            return false;
        } 
        PartyJoinedEvent partyJoinedEvent = (PartyJoinedEvent) o;
        if(!(this.getService() == partyJoinedEvent.getService())) {
            return false;
        }
        if ((this.tpConfCallIdentifier != null) && (partyJoinedEvent.tpConfCallIdentifier != null)) {
            if(!(this.tpConfCallIdentifier.equals(partyJoinedEvent.tpConfCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (partyJoinedEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(partyJoinedEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.eventInfo != null) && (partyJoinedEvent.eventInfo != null)) {
            if(!(this.eventInfo.equals(partyJoinedEvent.eventInfo)))  {
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
    private org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier tpCallLegIdentifier = null;
    private org.csapi.cc.cccs.TpJoinEventInfo eventInfo = null;

} // PartyJoinedEvent

