package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a call supervision error to the application.
 * 
 * 
 */
public class SuperviseCallErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseCallErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseCallErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.TpCallError errorIndication ){
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
        if(!(o instanceof SuperviseCallErrEvent)) {
            return false;
        } 
        SuperviseCallErrEvent superviseCallErrEvent = (SuperviseCallErrEvent) o;
        if(!(this.getService() == superviseCallErrEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (superviseCallErrEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(superviseCallErrEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (superviseCallErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(superviseCallErrEvent.errorIndication)))  {
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

} // SuperviseCallErrEvent

