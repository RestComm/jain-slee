package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to connect a data session with the destination party was unsuccessful, e.g, an error detected in the network or the data session was abandoned.
 * 
 * 
 */
public class ConnectErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ConnectErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ConnectErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier tpDataSessionIdentifier , org.csapi.dsc.TpDataSessionError errorIndication , int assignmentID ){
        super(tpServiceIdentifier);
        this.tpDataSessionIdentifier = tpDataSessionIdentifier;
        this.errorIndication = errorIndication;
        this.assignmentID = assignmentID;
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
     * Returns the assignmentID
     * 
     */
    public int getAssignmentID() {
        return this.assignmentID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ConnectErrEvent)) {
            return false;
        } 
        ConnectErrEvent connectErrEvent = (ConnectErrEvent) o;
        if(!(this.getService() == connectErrEvent.getService())) {
            return false;
        }
        if ((this.tpDataSessionIdentifier != null) && (connectErrEvent.tpDataSessionIdentifier != null)) {
            if(!(this.tpDataSessionIdentifier.equals(connectErrEvent.tpDataSessionIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (connectErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(connectErrEvent.errorIndication)))  {
                return false;
            }
        }
        if(!(this.assignmentID == connectErrEvent.assignmentID)) {
            return false;
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
    private int assignmentID;

} // ConnectErrEvent

