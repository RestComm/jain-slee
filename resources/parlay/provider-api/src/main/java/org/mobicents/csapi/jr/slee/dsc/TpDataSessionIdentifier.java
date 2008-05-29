package org.mobicents.csapi.jr.slee.dsc;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Data Session object 
 * 
 */
public final class TpDataSessionIdentifier implements java.io.Serializable {

    private int dataSessionRefID;
    private int dataSessionID;


    /**
     * Creates a new TpDataSessionIdentifier instance.
     * @param _dataSessionRefID   This element specifies the interface reference for the Data Session object.
     * @param _dataSessionID   This element specifies the data session ID of the Data Session.
     */
    public TpDataSessionIdentifier (int _dataSessionRefID, int _dataSessionID) {

        this.dataSessionRefID = _dataSessionRefID;
        this.dataSessionID = _dataSessionID;
    }

    /**
     * This method returns a dataSessionReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.dsc.IpDataSession value
     */
    public int getDataSessionRefID() {
        return dataSessionRefID;
    }

    /**
     * This method returns a dataSessionID.
     * 
     * @return a int value
     */
    public int getDataSessionID() {
        return dataSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpDataSessionIdentifier)) {
            return false;
        } 
        TpDataSessionIdentifier tpDataSessionIdentifier = (TpDataSessionIdentifier) o;
        if(!(this.dataSessionID == tpDataSessionIdentifier.dataSessionID)) {
            return false;
        }
        if(!(this.dataSessionRefID == tpDataSessionIdentifier.dataSessionRefID))  {
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

} // TpDataSessionIdentifier

