package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports an error in collecting digits to the application.
 * 
 * 
 */
public class GetMoreDialledDigitsErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for GetMoreDialledDigitsErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public GetMoreDialledDigitsErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.TpCallError errorIndication ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.errorIndication = errorIndication;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
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
        if(!(o instanceof GetMoreDialledDigitsErrEvent)) {
            return false;
        } 
        GetMoreDialledDigitsErrEvent getMoreDialledDigitsErrEvent = (GetMoreDialledDigitsErrEvent) o;
        if(!(this.getService() == getMoreDialledDigitsErrEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (getMoreDialledDigitsErrEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(getMoreDialledDigitsErrEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (getMoreDialledDigitsErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(getMoreDialledDigitsErrEvent.errorIndication)))  {
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
    private org.csapi.cc.TpCallError errorIndication = null;

} // GetMoreDialledDigitsErrEvent

