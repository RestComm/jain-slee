package org.mobicents.csapi.jr.slee.cc.mmccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a call supervision error to the application.
 * 
 * 
 */
public class SuperviseVolumeErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseVolumeErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseVolumeErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiMediaCallIdentifier tpMultiMediaCallIdentifier , org.csapi.cc.TpCallError errorIndication ){
        super(tpServiceIdentifier);
        this.tpMultiMediaCallIdentifier = tpMultiMediaCallIdentifier;
        this.errorIndication = errorIndication;
    }

    /**
     * Returns the tpMultiMediaCallIdentifier
     * 
     */
    public TpMultiMediaCallIdentifier getTpMultiMediaCallIdentifier() {
        return this.tpMultiMediaCallIdentifier;
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
        if(!(o instanceof SuperviseVolumeErrEvent)) {
            return false;
        } 
        SuperviseVolumeErrEvent superviseVolumeErrEvent = (SuperviseVolumeErrEvent) o;
        if(!(this.getService() == superviseVolumeErrEvent.getService())) {
            return false;
        }
        if ((this.tpMultiMediaCallIdentifier != null) && (superviseVolumeErrEvent.tpMultiMediaCallIdentifier != null)) {
            if(!(this.tpMultiMediaCallIdentifier.equals(superviseVolumeErrEvent.tpMultiMediaCallIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (superviseVolumeErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(superviseVolumeErrEvent.errorIndication)))  {
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

    private TpMultiMediaCallIdentifier tpMultiMediaCallIdentifier = null;
    private org.csapi.cc.TpCallError errorIndication = null;

} // SuperviseVolumeErrEvent

