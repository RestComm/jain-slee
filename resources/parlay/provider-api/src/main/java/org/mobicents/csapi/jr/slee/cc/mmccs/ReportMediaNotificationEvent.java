package org.mobicents.csapi.jr.slee.cc.mmccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event is used to inform the application about the establishment of media streams.
If the corresponding monitor was in interrupt mode, then the application has to allow or deny the streams using mediaStreamAllow() method.  If the application has previously explicitly passed a reference to the callback using a setCallbackWithSessionID() invocation, this parameter may be P_APP_CALLBACK_UNDEFINED, or if supplied must be the same as that provided during the setCallbackWithSessionID().
@return appMultiMediaCallBack: Specifies references to the application interface which implements the callback interface for the new multi-media call and/or new call leg.  This parameter may be null if the notification is being given in NOTIFY mode
 * 
 * 
 */
public class ReportMediaNotificationEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReportMediaNotificationEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReportMediaNotificationEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiMediaCallIdentifier callReference , TpMultiMediaCallLegIdentifier[] callLegReferenceSet , org.csapi.cc.mmccs.TpMediaStream[] mediaStreams , org.csapi.cc.mmccs.TpMediaStreamEventType type , int assignmentID ){
        super(tpServiceIdentifier);
        this.callReference = callReference;
        this.callLegReferenceSet = callLegReferenceSet;
        this.mediaStreams = mediaStreams;
        this.type = type;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the callReference
     * 
     */
    public TpMultiMediaCallIdentifier getCallReference() {
        return this.callReference;
    }
    /**
     * Returns the callLegReferenceSet
     * 
     */
    public TpMultiMediaCallLegIdentifier[] getCallLegReferenceSet() {
        return this.callLegReferenceSet;
    }
    /**
     * Returns the mediaStreams
     * 
     */
    public org.csapi.cc.mmccs.TpMediaStream[] getMediaStreams() {
        return this.mediaStreams;
    }
    /**
     * Returns the type
     * 
     */
    public org.csapi.cc.mmccs.TpMediaStreamEventType getType() {
        return this.type;
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
        if(!(o instanceof ReportMediaNotificationEvent)) {
            return false;
        } 
        ReportMediaNotificationEvent reportMediaNotificationEvent = (ReportMediaNotificationEvent) o;
        if(!(this.getService() == reportMediaNotificationEvent.getService())) {
            return false;
        }
        if ((this.callReference != null) && (reportMediaNotificationEvent.callReference != null)) {
            if(!(this.callReference.equals(reportMediaNotificationEvent.callReference)))  {
                return false;
            }
        }
        if ((this.callLegReferenceSet != null) && (reportMediaNotificationEvent.callLegReferenceSet != null)) {
            if(!(this.callLegReferenceSet.equals(reportMediaNotificationEvent.callLegReferenceSet)))  {
                return false;
            }
        }
        if ((this.mediaStreams != null) && (reportMediaNotificationEvent.mediaStreams != null)) {
            if(!(this.mediaStreams.equals(reportMediaNotificationEvent.mediaStreams)))  {
                return false;
            }
        }
        if ((this.type != null) && (reportMediaNotificationEvent.type != null)) {
            if(!(this.type.equals(reportMediaNotificationEvent.type)))  {
                return false;
            }
        }
        if(!(this.assignmentID == reportMediaNotificationEvent.assignmentID)) {
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

    private TpMultiMediaCallIdentifier callReference = null;
    private TpMultiMediaCallLegIdentifier[] callLegReferenceSet = null;
    private org.csapi.cc.mmccs.TpMediaStream[] mediaStreams = null;
    private org.csapi.cc.mmccs.TpMediaStreamEventType type = null;
    private int assignmentID;

} // ReportMediaNotificationEvent

