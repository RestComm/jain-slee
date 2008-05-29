package org.mobicents.csapi.jr.slee.cc.mpccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Call object 
 * 
 */
public final class TpMultiPartyCallIdentifier implements java.io.Serializable {

    private int callRefID;
    private int callSessionID;


    /**
     * Creates a new TpMultiPartyCallIdentifier instance.
     * @param _callRefID   This element specifies the interface reference for the Multi-party call object.
     * @param _callSessionID   This element specifies the call session ID.
     */
    public TpMultiPartyCallIdentifier (int _callRefID, int _callSessionID) {

        this.callRefID = _callRefID;
        this.callSessionID = _callSessionID;
    }

    /**
     * This method returns a callReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCall value
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
        if(!(o instanceof TpMultiPartyCallIdentifier)) {
            return false;
        } 
        TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier = (TpMultiPartyCallIdentifier) o;
        if(!(this.callSessionID == tpMultiPartyCallIdentifier.callSessionID)) {
            return false;
        }
        if(!(this.callRefID == tpMultiPartyCallIdentifier.callRefID))  {
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

} // TpMultiPartyCallIdentifier

