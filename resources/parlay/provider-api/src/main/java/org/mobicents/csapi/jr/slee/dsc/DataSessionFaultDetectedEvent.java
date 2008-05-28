package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that a fault in the network has been detected which cannot be communicated by a network event, e.g., when the user aborts before any establishment method is called by the application.
The system purges the Data Session object. Therefore, the application has no further control of data session processing. No report will be forwarded to the application.
 * 
 * 
 */
public class DataSessionFaultDetectedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DataSessionFaultDetectedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DataSessionFaultDetectedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier tpDataSessionIdentifier , org.csapi.dsc.TpDataSessionFault fault ){
        super(tpServiceIdentifier);
        this.tpDataSessionIdentifier = tpDataSessionIdentifier;
        this.fault = fault;
    }

    /**
     * Returns the tpDataSessionIdentifier
     * 
     */
    public TpDataSessionIdentifier getTpDataSessionIdentifier() {
        return this.tpDataSessionIdentifier;
    }
    /**
     * Returns the fault
     * 
     */
    public org.csapi.dsc.TpDataSessionFault getFault() {
        return this.fault;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof DataSessionFaultDetectedEvent)) {
            return false;
        } 
        DataSessionFaultDetectedEvent dataSessionFaultDetectedEvent = (DataSessionFaultDetectedEvent) o;
        if(!(this.getService() == dataSessionFaultDetectedEvent.getService())) {
            return false;
        }
        if ((this.tpDataSessionIdentifier != null) && (dataSessionFaultDetectedEvent.tpDataSessionIdentifier != null)) {
            if(!(this.tpDataSessionIdentifier.equals(dataSessionFaultDetectedEvent.tpDataSessionIdentifier)))  {
                return false;
            }
        }
        if ((this.fault != null) && (dataSessionFaultDetectedEvent.fault != null)) {
            if(!(this.fault.equals(dataSessionFaultDetectedEvent.fault)))  {
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
    private org.csapi.dsc.TpDataSessionFault fault = null;

} // DataSessionFaultDetectedEvent

