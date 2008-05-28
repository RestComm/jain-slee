package org.mobicents.csapi.jr.slee.gms;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that a fault has been detected in the mailbox. 
 * 
 * 
 */
public class MailboxFaultDetectedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for MailboxFaultDetectedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public MailboxFaultDetectedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMailboxIdentifier tpMailboxIdentifier , org.csapi.gms.TpMessagingFault fault ){
        super(tpServiceIdentifier);
        this.tpMailboxIdentifier = tpMailboxIdentifier;
        this.fault = fault;
    }

    /**
     * Returns the tpMailboxIdentifier
     * 
     */
    public TpMailboxIdentifier getTpMailboxIdentifier() {
        return this.tpMailboxIdentifier;
    }
    /**
     * Returns the fault
     * 
     */
    public org.csapi.gms.TpMessagingFault getFault() {
        return this.fault;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof MailboxFaultDetectedEvent)) {
            return false;
        } 
        MailboxFaultDetectedEvent mailboxFaultDetectedEvent = (MailboxFaultDetectedEvent) o;
        if(!(this.getService() == mailboxFaultDetectedEvent.getService())) {
            return false;
        }
        if ((this.tpMailboxIdentifier != null) && (mailboxFaultDetectedEvent.tpMailboxIdentifier != null)) {
            if(!(this.tpMailboxIdentifier.equals(mailboxFaultDetectedEvent.tpMailboxIdentifier)))  {
                return false;
            }
        }
        if ((this.fault != null) && (mailboxFaultDetectedEvent.fault != null)) {
            if(!(this.fault.equals(mailboxFaultDetectedEvent.fault)))  {
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

    private TpMailboxIdentifier tpMailboxIdentifier = null;
    private org.csapi.gms.TpMessagingFault fault = null;

} // MailboxFaultDetectedEvent

