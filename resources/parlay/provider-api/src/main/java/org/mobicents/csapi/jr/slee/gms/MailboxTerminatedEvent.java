package org.mobicents.csapi.jr.slee.gms;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the mailbox has terminated or closed abnormally. No further communication will be possible between the mailbox and application. 
 * 
 * 
 */
public class MailboxTerminatedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for MailboxTerminatedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public MailboxTerminatedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMailboxIdentifier tpMailboxIdentifier ){
        super(tpServiceIdentifier);
        this.tpMailboxIdentifier = tpMailboxIdentifier;
    }

    /**
     * Returns the tpMailboxIdentifier
     * 
     */
    public TpMailboxIdentifier getTpMailboxIdentifier() {
        return this.tpMailboxIdentifier;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof MailboxTerminatedEvent)) {
            return false;
        } 
        MailboxTerminatedEvent mailboxTerminatedEvent = (MailboxTerminatedEvent) o;
        if(!(this.getService() == mailboxTerminatedEvent.getService())) {
            return false;
        }
        if ((this.tpMailboxIdentifier != null) && (mailboxTerminatedEvent.tpMailboxIdentifier != null)) {
            if(!(this.tpMailboxIdentifier.equals(mailboxTerminatedEvent.tpMailboxIdentifier)))  {
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

} // MailboxTerminatedEvent

