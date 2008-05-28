package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports the attachment of a call leg to a call has succeeded.  The media channels or bearer connections to this leg is now available.
 * 
 * 
 */
public class AttachMediaResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for AttachMediaResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public AttachMediaResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier ){
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
        if(!(o instanceof AttachMediaResEvent)) {
            return false;
        } 
        AttachMediaResEvent attachMediaResEvent = (AttachMediaResEvent) o;
        if(!(this.getService() == attachMediaResEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (attachMediaResEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(attachMediaResEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (attachMediaResEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(attachMediaResEvent.tpCallLegIdentifier)))  {
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

} // AttachMediaResEvent

