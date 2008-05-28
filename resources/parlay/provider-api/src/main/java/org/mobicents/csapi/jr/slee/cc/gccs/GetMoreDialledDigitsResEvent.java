package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event returns the collected digits to the application.
 * 
 * 
 */
public class GetMoreDialledDigitsResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for GetMoreDialledDigitsResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public GetMoreDialledDigitsResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , String digits ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.digits = digits;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }
    /**
     * Returns the digits
     * 
     */
    public String getDigits() {
        return this.digits;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof GetMoreDialledDigitsResEvent)) {
            return false;
        } 
        GetMoreDialledDigitsResEvent getMoreDialledDigitsResEvent = (GetMoreDialledDigitsResEvent) o;
        if(!(this.getService() == getMoreDialledDigitsResEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (getMoreDialledDigitsResEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(getMoreDialledDigitsResEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.digits != null) && (getMoreDialledDigitsResEvent.digits != null)) {
            if(!(this.digits.equals(getMoreDialledDigitsResEvent.digits)))  {
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

    private TpCallIdentifier tpCallIdentifier = null;
    private String digits = null;

} // GetMoreDialledDigitsResEvent

