package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to connect a data session with the destination party was successful, and indicates the response of the destination party (e.g, connected, disconnected).
 * 
 * 
 */
public class ConnectResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ConnectResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ConnectResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier tpDataSessionIdentifier , org.csapi.dsc.TpDataSessionReport eventReport , int assignmentID ){
        super(tpServiceIdentifier);
        this.tpDataSessionIdentifier = tpDataSessionIdentifier;
        this.eventReport = eventReport;
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
     * Returns the eventReport
     * 
     */
    public org.csapi.dsc.TpDataSessionReport getEventReport() {
        return this.eventReport;
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
        if(!(o instanceof ConnectResEvent)) {
            return false;
        } 
        ConnectResEvent connectResEvent = (ConnectResEvent) o;
        if(!(this.getService() == connectResEvent.getService())) {
            return false;
        }
        if ((this.tpDataSessionIdentifier != null) && (connectResEvent.tpDataSessionIdentifier != null)) {
            if(!(this.tpDataSessionIdentifier.equals(connectResEvent.tpDataSessionIdentifier)))  {
                return false;
            }
        }
        if ((this.eventReport != null) && (connectResEvent.eventReport != null)) {
            if(!(this.eventReport.equals(connectResEvent.eventReport)))  {
                return false;
            }
        }
        if(!(this.assignmentID == connectResEvent.assignmentID)) {
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
    private org.csapi.dsc.TpDataSessionReport eventReport = null;
    private int assignmentID;

} // ConnectResEvent

