package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports time information of the finished call or call attempt as well as release cause depending on which information has been requested by getCallInfoReq. This information may be used e.g. for charging purposes. The call information will possibly be sent after routeRes in all cases where the call or a leg of the call has been disconnected or a routing failure has been encountered.			
 * 
 * 
 */
public class GetCallInfoResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for GetCallInfoResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public GetCallInfoResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.gccs.TpCallInfoReport callInfoReport ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.callInfoReport = callInfoReport;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }
    /**
     * Returns the callInfoReport
     * 
     */
    public org.csapi.cc.gccs.TpCallInfoReport getCallInfoReport() {
        return this.callInfoReport;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof GetCallInfoResEvent)) {
            return false;
        } 
        GetCallInfoResEvent getCallInfoResEvent = (GetCallInfoResEvent) o;
        if(!(this.getService() == getCallInfoResEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (getCallInfoResEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(getCallInfoResEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.callInfoReport != null) && (getCallInfoResEvent.callInfoReport != null)) {
            if(!(this.callInfoReport.equals(getCallInfoResEvent.callInfoReport)))  {
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

    private TpCallIdentifier tpCallIdentifier = null;
    private org.csapi.cc.gccs.TpCallInfoReport callInfoReport = null;

} // GetCallInfoResEvent

