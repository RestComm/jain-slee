package org.mobicents.csapi.jr.slee.cc.mmccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the MultiMediaCall object. 
 * 
 */
public final class TpMultiMediaCallIdentifier implements java.io.Serializable {

    private int mMCallRefID;
    private int mMCallSessionID;


    /**
     * Creates a new TpMultiMediaCallIdentifier instance.
     * @param _mMCallRefID   This element specifies the interface reference for the call object.
     * @param _mMCallSessionID   This element specifies the call session ID of the call created.
     */
    public TpMultiMediaCallIdentifier (int _mMCallRefID, int _mMCallSessionID) {

        this.mMCallRefID = _mMCallRefID;
        this.mMCallSessionID = _mMCallSessionID;
    }

    /**
     * This method returns a mMCallReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.mmccs.IpMultiMediaCall value
     */
    public int getMMCallRefID() {
        return mMCallRefID;
    }

    /**
     * This method returns a mMCallSessionID.
     * 
     * @return a int value
     */
    public int getMMCallSessionID() {
        return mMCallSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpMultiMediaCallIdentifier)) {
            return false;
        } 
        TpMultiMediaCallIdentifier tpMultiMediaCallIdentifier = (TpMultiMediaCallIdentifier) o;
        if(!(this.mMCallRefID == tpMultiMediaCallIdentifier.mMCallRefID))  {
            return false;
        }
        if(!(this.mMCallSessionID == tpMultiMediaCallIdentifier.mMCallSessionID)) {
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

} // TpMultiMediaCallIdentifier

