package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports that the original request was erroneous, or resulted in an error condition.
 * 
 * 
 */
public class GetCallInfoErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for GetCallInfoErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public GetCallInfoErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.TpCallError errorIndication ){
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
        if(!(o instanceof GetCallInfoErrEvent)) {
            return false;
        } 
        GetCallInfoErrEvent getCallInfoErrEvent = (GetCallInfoErrEvent) o;
        if(!(this.getService() == getCallInfoErrEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (getCallInfoErrEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(getCallInfoErrEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (getCallInfoErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(getCallInfoErrEvent.errorIndication)))  {
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

} // GetCallInfoErrEvent

