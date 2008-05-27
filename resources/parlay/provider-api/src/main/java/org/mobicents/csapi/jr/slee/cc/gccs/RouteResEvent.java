package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to route the call to the destination was successful, and indicates the response of the destination party (for example, the call was answered, not answered, refused due to busy, etc.).
If this method is invoked with a monitor mode of P_CALL_MONITOR_MODE_INTERRUPT, 
then the APL has control of the call. If the APL does nothing with the call (including its associated legs) within a specified time period (the duration of which forms a part of the service level agreement), then the call in the network shall be released and callEnded() shall be invoked, giving a release cause of 102 (Recovery on timer expiry).
 * 
 * 
 */
public class RouteResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RouteResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RouteResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.gccs.TpCallReport eventReport , int callLegSessionID ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.eventReport = eventReport;
        this.callLegSessionID = callLegSessionID;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }
    /**
     * Returns the eventReport
     * 
     */
    public org.csapi.cc.gccs.TpCallReport getEventReport() {
        return this.eventReport;
    }
    /**
     * Returns the callLegSessionID
     * 
     */
    public int getCallLegSessionID() {
        return this.callLegSessionID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof RouteResEvent)) {
            return false;
        } 
        RouteResEvent routeResEvent = (RouteResEvent) o;
        if(!(this.getService() == routeResEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (routeResEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(routeResEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.eventReport != null) && (routeResEvent.eventReport != null)) {
            if(!(this.eventReport.equals(routeResEvent.eventReport)))  {
                return false;
            }
        }
        if(!(this.callLegSessionID == routeResEvent.callLegSessionID)) {
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

    private TpCallIdentifier tpCallIdentifier = null;
    private org.csapi.cc.gccs.TpCallReport eventReport = null;
    private int callLegSessionID;

} // RouteResEvent

