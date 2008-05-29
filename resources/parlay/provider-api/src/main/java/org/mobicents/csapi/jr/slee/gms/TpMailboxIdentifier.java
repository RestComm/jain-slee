package org.mobicents.csapi.jr.slee.gms;

/**
 * Defines  the Sequence of Data Elements  that identify a mailbox. 
 * 
 */
public final class TpMailboxIdentifier implements java.io.Serializable {

    private int mailbox;
    private int sessionID;


    /**
     * Creates a new TpMailboxIdentifier instance.
     * @param _mailbox   
     * @param _sessionID   
     */
    public TpMailboxIdentifier (int _mailbox, int _sessionID) {

        this.mailbox = _mailbox;
        this.sessionID = _sessionID;
    }

    /**
     * This method returns a mailbox.
     * 
     * @return a org.mobicents.csapi.jr.slee.gms.IpMailbox value
     */
    public int getMailbox() {
        return mailbox;
    }

    /**
     * This method returns a sessionID.
     * 
     * @return a int value
     */
    public int getSessionID() {
        return sessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpMailboxIdentifier)) {
            return false;
        } 
        TpMailboxIdentifier tpMailboxIdentifier = (TpMailboxIdentifier) o;
        if(!(this.mailbox == tpMailboxIdentifier.mailbox))  {
            return false;
        }
        if(!(this.sessionID == tpMailboxIdentifier.sessionID)) {
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

} // TpMailboxIdentifier

