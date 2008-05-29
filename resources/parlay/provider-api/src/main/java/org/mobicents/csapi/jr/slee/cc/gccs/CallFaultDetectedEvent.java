package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that a fault in the network has been detected. The call may or may not have been terminated.
The system deletes the call object. Therefore, the application has no further control of call processing. No report will be forwarded to the application.
 * 
 * 
 */
public class CallFaultDetectedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallFaultDetectedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallFaultDetectedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , org.csapi.cc.gccs.TpCallFault fault ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.fault = fault;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }
    /**
     * Returns the fault
     * 
     */
    public org.csapi.cc.gccs.TpCallFault getFault() {
        return this.fault;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CallFaultDetectedEvent)) {
            return false;
        } 
        CallFaultDetectedEvent callFaultDetectedEvent = (CallFaultDetectedEvent) o;
        if(!(this.getService() == callFaultDetectedEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (callFaultDetectedEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(callFaultDetectedEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if ((this.fault != null) && (callFaultDetectedEvent.fault != null)) {
            if(!(this.fault.equals(callFaultDetectedEvent.fault)))  {
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
    private org.csapi.cc.gccs.TpCallFault fault = null;

} // CallFaultDetectedEvent

