package org.mobicents.csapi.jr.slee.cc.cccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the SubConference Call object 
 * 
 */
public final class TpSubConfCallIdentifier implements java.io.Serializable {

    private int subConfCallRefID;
    private int subConfCallSessionID;


    /**
     * Creates a new TpSubConfCallIdentifier instance.
     * @param _subConfCallRefID   This element specifies the interface reference for the subconference call object.
     * @param _subConfCallSessionID   This element specifies the session ID of the subconference call.
     */
    public TpSubConfCallIdentifier (int _subConfCallRefID, int _subConfCallSessionID) {

        this.subConfCallRefID = _subConfCallRefID;
        this.subConfCallSessionID = _subConfCallSessionID;
    }

    /**
     * This method returns a subConfCallReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.cccs.IpSubConfCall value
     */
    public int getSubConfCallRefID() {
        return subConfCallRefID;
    }

    /**
     * This method returns a subConfCallSessionID.
     * 
     * @return a int value
     */
    public int getSubConfCallSessionID() {
        return subConfCallSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpSubConfCallIdentifier)) {
            return false;
        } 
        TpSubConfCallIdentifier tpSubConfCallIdentifier = (TpSubConfCallIdentifier) o;
        if(!(this.subConfCallRefID == tpSubConfCallIdentifier.subConfCallRefID))  {
            return false;
        }
        if(!(this.subConfCallSessionID == tpSubConfCallIdentifier.subConfCallSessionID)) {
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

} // TpSubConfCallIdentifier

