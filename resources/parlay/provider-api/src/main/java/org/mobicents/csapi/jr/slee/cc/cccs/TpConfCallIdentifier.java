package org.mobicents.csapi.jr.slee.cc.cccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Conference Call object 
 * 
 */
public final class TpConfCallIdentifier implements java.io.Serializable {

    private int confCallRefID;
    private int confCallSessionID;


    /**
     * Creates a new TpConfCallIdentifier instance.
     * @param _confCallRefID   This element specifies the interface reference for the conference call object.
     * @param _confCallSessionID   This element specifies the session ID of the conference call.
     */
    public TpConfCallIdentifier (int _confCallRefID, int _confCallSessionID) {

        this.confCallRefID = _confCallRefID;
        this.confCallSessionID = _confCallSessionID;
    }

    /**
     * This method returns a confCallReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.cccs.IpConfCall value
     */
    public int getConfCallRefID() {
        return confCallRefID;
    }

    /**
     * This method returns a confCallSessionID.
     * 
     * @return a int value
     */
    public int getConfCallSessionID() {
        return confCallSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpConfCallIdentifier)) {
            return false;
        } 
        TpConfCallIdentifier tpConfCallIdentifier = (TpConfCallIdentifier) o;
        if(!(this.confCallRefID == tpConfCallIdentifier.confCallRefID))  {
            return false;
        }
        if(!(this.confCallSessionID == tpConfCallIdentifier.confCallSessionID)) {
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

} // TpConfCallIdentifier

