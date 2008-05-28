package org.mobicents.csapi.jr.slee.cc.mmccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Call Leg object.
 * 
 */
public final class TpMultiMediaCallLegIdentifier implements java.io.Serializable {

    private int mMCallLegRefID;
    private int mMCallLegSessionID;


    /**
     * Creates a new TpMultiMediaCallLegIdentifier instance.
     * @param _mMCallLegRefID   This element specifies the interface reference for the callLeg object.
     * @param _mMCallLegSessionID   This element specifies the callLeg session ID of the call created.
     */
    public TpMultiMediaCallLegIdentifier (int _mMCallLegRefID, int _mMCallLegSessionID) {

        this.mMCallLegRefID = _mMCallLegRefID;
        this.mMCallLegSessionID = _mMCallLegSessionID;
    }

    /**
     * This method returns a mMCallLegReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.mmccs.IpMultiMediaCallLeg value
     */
    public int getMMCallLegRefID() {
        return mMCallLegRefID;
    }

    /**
     * This method returns a mMCallLegSessionID.
     * 
     * @return a int value
     */
    public int getMMCallLegSessionID() {
        return mMCallLegSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpMultiMediaCallLegIdentifier)) {
            return false;
        } 
        TpMultiMediaCallLegIdentifier tpMultiMediaCallLegIdentifier = (TpMultiMediaCallLegIdentifier) o;
        if(!(this.mMCallLegRefID == tpMultiMediaCallLegIdentifier.mMCallLegRefID))  {
            return false;
        }
        if(!(this.mMCallLegSessionID == tpMultiMediaCallLegIdentifier.mMCallLegSessionID)) {
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

} // TpMultiMediaCallLegIdentifier

