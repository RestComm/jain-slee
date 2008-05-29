package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports the detachment of a call leg from a call has succeeded. The media channels or bearer connections  to this leg is no longer available.
 * 
 * 
 */
public class DetachMediaResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DetachMediaResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DetachMediaResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
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
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof DetachMediaResEvent)) {
            return false;
        } 
        DetachMediaResEvent detachMediaResEvent = (DetachMediaResEvent) o;
        if(!(this.getService() == detachMediaResEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (detachMediaResEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(detachMediaResEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (detachMediaResEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(detachMediaResEvent.tpCallLegIdentifier)))  {
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

} // DetachMediaResEvent

