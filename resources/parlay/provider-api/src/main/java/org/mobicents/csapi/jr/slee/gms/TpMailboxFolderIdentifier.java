package org.mobicents.csapi.jr.slee.gms;

/**
 * Defines the Sequence of Data Elements that identify a folder. 
 * 
 */
public final class TpMailboxFolderIdentifier implements java.io.Serializable {

    private int mailboxFolder;
    private int sessionID;


    /**
     * Creates a new TpMailboxFolderIdentifier instance.
     * @param _mailboxFolder   
     * @param _sessionID   
     */
    public TpMailboxFolderIdentifier (int _mailboxFolder, int _sessionID) {

        this.mailboxFolder = _mailboxFolder;
        this.sessionID = _sessionID;
    }

    /**
     * This method returns a mailboxFolder.
     * 
     * @return a org.mobicents.csapi.jr.slee.gms.IpMailboxFolder value
     */
    public int getMailboxFolder() {
        return mailboxFolder;
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
        if(!(o instanceof TpMailboxFolderIdentifier)) {
            return false;
        } 
        TpMailboxFolderIdentifier tpMailboxFolderIdentifier = (TpMailboxFolderIdentifier) o;
        if(!(this.sessionID == tpMailboxFolderIdentifier.sessionID)) {
            return false;
        }
        if(!(this.mailboxFolder == tpMailboxFolderIdentifier.mailboxFolder))  {
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

} // TpMailboxFolderIdentifier

