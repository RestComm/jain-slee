package org.mobicents.csapi.jr.slee.cc.gccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Generic Call object
 * 
 */
public final class TpCallIdentifier implements java.io.Serializable {

    private int callRefID;
    private int callSessionID;


    /**
     * Creates a new TpCallIdentifier instance.
     * @param _callRefID   This element specifies the interface reference for the call object.
     * @param _callSessionID   This element specifies the call session ID of the call.
     */
    public TpCallIdentifier (int _callRefID, int _callSessionID) {

        this.callRefID = _callRefID;
        this.callSessionID = _callSessionID;
    }

    /**
     * This method returns a callReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.gccs.IpCall value
     */
    public int getCallRefID() {
        return callRefID;
    }

    /**
     * This method returns a callSessionID.
     * 
     * @return a int value
     */
    public int getCallSessionID() {
        return callSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpCallIdentifier)) {
            return false;
        } 
        TpCallIdentifier tpCallIdentifier = (TpCallIdentifier) o;
        if(!(this.callRefID == tpCallIdentifier.callRefID))  {
            return false;
        }
        if(!(this.callSessionID == tpCallIdentifier.callSessionID)) {
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

} // TpCallIdentifier

