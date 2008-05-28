package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that a fault has been detected in the user interaction. 
 * 
 * 
 */
public class UserInteractionFaultDetectedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for UserInteractionFaultDetectedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public UserInteractionFaultDetectedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier, TpUIIdentifier tpUIIdentifier , org.csapi.ui.TpUIFault fault ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.tpUIIdentifier = tpUIIdentifier;
        this.fault = fault;
    }

    /**
     * Returns the tpUICallIdentifier
     * 
     */
    public TpUICallIdentifier getTpUICallIdentifier() {
        return this.tpUICallIdentifier;
    }
    /**
     * Returns the tpUIIdentifier
     * 
     */
    public TpUIIdentifier getTpUIIdentifier() {
        return this.tpUIIdentifier;
    }
    /**
     * Returns the fault
     * 
     */
    public org.csapi.ui.TpUIFault getFault() {
        return this.fault;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof UserInteractionFaultDetectedEvent)) {
            return false;
        } 
        UserInteractionFaultDetectedEvent userInteractionFaultDetectedEvent = (UserInteractionFaultDetectedEvent) o;
        if(!(this.getService() == userInteractionFaultDetectedEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (userInteractionFaultDetectedEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(userInteractionFaultDetectedEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpUIIdentifier != null) && (userInteractionFaultDetectedEvent.tpUIIdentifier != null)) {
            if(!(this.tpUIIdentifier.equals(userInteractionFaultDetectedEvent.tpUIIdentifier)))  {
                return false;
            }
        }
        if ((this.fault != null) && (userInteractionFaultDetectedEvent.fault != null)) {
            if(!(this.fault.equals(userInteractionFaultDetectedEvent.fault)))  {
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

    private TpUICallIdentifier tpUICallIdentifier = null;
    private TpUIIdentifier tpUIIdentifier = null;
    private org.csapi.ui.TpUIFault fault = null;

} // UserInteractionFaultDetectedEvent

