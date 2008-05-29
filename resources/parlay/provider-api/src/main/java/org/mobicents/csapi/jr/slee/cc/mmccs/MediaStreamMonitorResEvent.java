package org.mobicents.csapi.jr.slee.cc.mmccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event is used to inform the application about the media streams that are being established (added) or subtracted.
If the corresponding request was done in interrupt mode, the application has to allow or deny the media streams using mediaStreamAllow().
 * 
 * 
 */
public class MediaStreamMonitorResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for MediaStreamMonitorResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public MediaStreamMonitorResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int callLegSessionID , org.csapi.cc.mmccs.TpMediaStream[] streams , org.csapi.cc.mmccs.TpMediaStreamEventType type ){
        super(tpServiceIdentifier);
        this.callLegSessionID = callLegSessionID;
        this.streams = streams;
        this.type = type;
    }

    /**
     * Returns the callLegSessionID
     * 
     */
    public int getCallLegSessionID() {
        return this.callLegSessionID;
    }
    /**
     * Returns the streams
     * 
     */
    public org.csapi.cc.mmccs.TpMediaStream[] getStreams() {
        return this.streams;
    }
    /**
     * Returns the type
     * 
     */
    public org.csapi.cc.mmccs.TpMediaStreamEventType getType() {
        return this.type;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof MediaStreamMonitorResEvent)) {
            return false;
        } 
        MediaStreamMonitorResEvent mediaStreamMonitorResEvent = (MediaStreamMonitorResEvent) o;
        if(!(this.getService() == mediaStreamMonitorResEvent.getService())) {
            return false;
        }
        if(!(this.callLegSessionID == mediaStreamMonitorResEvent.callLegSessionID)) {
            return false;
        }
        if ((this.streams != null) && (mediaStreamMonitorResEvent.streams != null)) {
            if(!(this.streams.equals(mediaStreamMonitorResEvent.streams)))  {
                return false;
            }
        }
        if ((this.type != null) && (mediaStreamMonitorResEvent.type != null)) {
            if(!(this.type.equals(mediaStreamMonitorResEvent.type)))  {
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

    private int callLegSessionID;
    private org.csapi.cc.mmccs.TpMediaStream[] streams = null;
    private org.csapi.cc.mmccs.TpMediaStreamEventType type = null;

} // MediaStreamMonitorResEvent

