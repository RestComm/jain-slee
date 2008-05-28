package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to route the call to the destination party was unsuccessful - the call could not be routed to the destination party (for example, the network was unable to route the call, the parameters were incorrect, the request was refused, etc.).
 * 
 * 
 */
public class RouteErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RouteErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RouteErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.TpCallError errorIndication , int callLegSessionID ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.errorIndication = errorIndication;
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
     * Returns the errorIndication
     * 
     */
    public org.csapi.cc.TpCallError getErrorIndication() {
        return this.errorIndication;
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
        if(!(o instanceof RouteErrEvent)) {
            return false;
        } 
        RouteErrEvent routeErrEvent = (RouteErrEvent) o;
        if(!(this.getService() == routeErrEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (routeErrEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(routeErrEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.errorIndication != null) && (routeErrEvent.errorIndication != null)) {
            if(!(this.errorIndication.equals(routeErrEvent.errorIndication)))  {
                return false;
            }
        }
        if(!(this.callLegSessionID == routeErrEvent.callLegSessionID)) {
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
    private org.csapi.cc.TpCallError errorIndication = null;
    private int callLegSessionID;

} // RouteErrEvent

