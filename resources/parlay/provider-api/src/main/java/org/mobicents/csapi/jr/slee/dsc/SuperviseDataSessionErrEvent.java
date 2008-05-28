package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a data session supervision error to the application.
 * 
 * 
 */
public class SuperviseDataSessionErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseDataSessionErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseDataSessionErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier tpDataSessionIdentifier , org.csapi.dsc.TpDataSessionError errorIndication ){
        super(tpServiceIdentifier);
        this.tpDataSessionIdentifier = tpDataSessionIdentifier;
        this.errorIndication = errorIndication;
    }

    /**
     * Returns the tpDataSessionIdentifier
     * 
     */
    public TpDataSessionIdentifier getTpDataSessionIdentifier() {
        return this.tpDataSessionIdentifier;
    }
    /**
     * Returns the errorIndication
     * 
     */
    public org.csapi.dsc.TpDataSessionError getErrorIndication() {
        return this.errorIndication;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SuperviseDataSessionErrEvent)) {
            return false;
        } 
        SuperviseDataSessionErrEvent superviseDataSessionErrEvent = (SuperviseDataSessionErrEvent) o;
        if(!(this.getService() == superviseDataSessionErrEvent.getService())) {
            return false;
        }
        if ((this.tpDataSessionIdentifier != null) && (superviseDataSessionErrEvent.tpDataSessionIdentifier != null)) {
            if(!(this.tpDataSessionIdentifier.equals(superviseDataSessionErrEvent.tpDataSessionIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (superviseDataSessionErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(superviseDataSessionErrEvent.errorIndication)))  {
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

    private TpDataSessionIdentifier tpDataSessionIdentifier = null;
    private org.csapi.dsc.TpDataSessionError errorIndication = null;

} // SuperviseDataSessionErrEvent

