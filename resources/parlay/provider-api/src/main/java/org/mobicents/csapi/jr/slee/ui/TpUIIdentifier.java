package org.mobicents.csapi.jr.slee.ui;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the UI object.
 * 
 */
public final class TpUIIdentifier implements java.io.Serializable {

    private int uIRefID;
    private int userInteractionSessionID;


    /**
     * Creates a new TpUIIdentifier instance.
     * @param _uIRefID   This element specifies the interface reference for the UI object.
     * @param _userInteractionSessionID   This element specifies the User Interaction session ID.
     */
    public TpUIIdentifier (int _uIRefID, int _userInteractionSessionID) {

        this.uIRefID = _uIRefID;
        this.userInteractionSessionID = _userInteractionSessionID;
    }

    /**
     * This method returns a uIRef.
     * 
     * @return a org.mobicents.csapi.jr.slee.ui.IpUI value
     */
    public int getUIRefID() {
        return uIRefID;
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
        if(!(o instanceof TpUIIdentifier)) {
            return false;
        } 
        TpUIIdentifier tpUIIdentifier = (TpUIIdentifier) o;
        if(!(this.userInteractionSessionID == tpUIIdentifier.userInteractionSessionID)) {
            return false;
        }
        if(!(this.uIRefID == tpUIIdentifier.uIRefID))  {
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

} // TpUIIdentifier

