package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the Data Session object has aborted or terminated abnormally. No further communication will be possible between the Data Session object and the application.
 * 
 * 
 */
public class DataSessionAbortedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DataSessionAbortedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DataSessionAbortedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier tpDataSessionIdentifier ){
        super(tpServiceIdentifier);
        this.tpDataSessionIdentifier = tpDataSessionIdentifier;
    }

    /**
     * Returns the tpDataSessionIdentifier
     * 
     */
    public TpDataSessionIdentifier getTpDataSessionIdentifier() {
        return this.tpDataSessionIdentifier;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof DataSessionAbortedEvent)) {
            return false;
        } 
        DataSessionAbortedEvent dataSessionAbortedEvent = (DataSessionAbortedEvent) o;
        if(!(this.getService() == dataSessionAbortedEvent.getService())) {
            return false;
        }
        if ((this.tpDataSessionIdentifier != null) && (dataSessionAbortedEvent.tpDataSessionIdentifier != null)) {
            if(!(this.tpDataSessionIdentifier.equals(dataSessionAbortedEvent.tpDataSessionIdentifier)))  {
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

} // DataSessionAbortedEvent

