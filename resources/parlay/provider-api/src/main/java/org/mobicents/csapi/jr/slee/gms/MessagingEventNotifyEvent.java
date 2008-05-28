package org.mobicents.csapi.jr.slee.gms;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event notifies the application of the arrival of a messaging-related event. 
 * 
 * 
 */
public class MessagingEventNotifyEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for MessagingEventNotifyEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public MessagingEventNotifyEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, org.csapi.gms.IpMessagingManager messagingManager , org.csapi.gms.TpMessagingEventInfo eventInfo , int assignmentID ){
        super(tpServiceIdentifier);
        this.messagingManager = messagingManager;
        this.eventInfo = eventInfo;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the messagingManager
     * 
     */
    public org.csapi.gms.IpMessagingManager getMessagingManager() {
        return this.messagingManager;
    }
    /**
     * Returns the eventInfo
     * 
     */
    public org.csapi.gms.TpMessagingEventInfo getEventInfo() {
        return this.eventInfo;
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
        if(!(o instanceof MessagingEventNotifyEvent)) {
            return false;
        } 
        MessagingEventNotifyEvent messagingEventNotifyEvent = (MessagingEventNotifyEvent) o;
        if(!(this.getService() == messagingEventNotifyEvent.getService())) {
            return false;
        }
        if ((this.messagingManager != null) && (messagingEventNotifyEvent.messagingManager != null)) {
            if(!(this.messagingManager.equals(messagingEventNotifyEvent.messagingManager)))  {
                return false;
            }
        }
        if ((this.eventInfo != null) && (messagingEventNotifyEvent.eventInfo != null)) {
            if(!(this.eventInfo.equals(messagingEventNotifyEvent.eventInfo)))  {
                return false;
            }
        }
        if(!(this.assignmentID == messagingEventNotifyEvent.assignmentID)) {
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

    private org.csapi.gms.IpMessagingManager messagingManager = null;
    private org.csapi.gms.TpMessagingEventInfo eventInfo = null;
    private int assignmentID;

} // MessagingEventNotifyEvent

