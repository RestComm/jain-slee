package org.mobicents.csapi.jr.slee.ui;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the UICall object.
 * 
 */
public final class TpUICallIdentifier implements java.io.Serializable {

    private int uICallRefID;
    private int userInteractionSessionID;


    /**
     * Creates a new TpUICallIdentifier instance.
     * @param _uICallRefID   This element specifies the interface reference for the UICall object.
     * @param _userInteractionSessionID   This element specifies the User Interaction session ID.
     */
    public TpUICallIdentifier (int _uICallRefID, int _userInteractionSessionID) {

        this.uICallRefID = _uICallRefID;
        this.userInteractionSessionID = _userInteractionSessionID;
    }

    /**
     * This method returns a uICallRef.
     * 
     * @return a org.mobicents.csapi.jr.slee.ui.IpUICall value
     */
    public int getUICallRefID() {
        return uICallRefID;
    }

    /**
     * This method returns a userInteractionSessionID.
     * 
     * @return a int value
     */
    public int getUserInteractionSessionID() {
        return userInteractionSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpUICallIdentifier)) {
            return false;
        } 
        TpUICallIdentifier tpUICallIdentifier = (TpUICallIdentifier) o;
        if(!(this.uICallRefID == tpUICallIdentifier.uICallRefID))  {
            return false;
        }
        if(!(this.userInteractionSessionID == tpUICallIdentifier.userInteractionSessionID)) {
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

} // TpUICallIdentifier

