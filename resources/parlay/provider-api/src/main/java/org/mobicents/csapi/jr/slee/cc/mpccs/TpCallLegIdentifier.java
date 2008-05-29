package org.mobicents.csapi.jr.slee.cc.mpccs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Call Leg object.
 * 
 */
public final class TpCallLegIdentifier implements java.io.Serializable {

    private int callLegRefID;
    private int callLegSessionID;


    /**
     * Creates a new TpCallLegIdentifier instance.
     * @param _callLegRefID   This element specifies the interface reference for the callLeg object.
     * @param _callLegSessionID   This element specifies the callLeg session ID.
     */
    public TpCallLegIdentifier (int _callLegRefID, int _callLegSessionID) {

        this.callLegRefID = _callLegRefID;
        this.callLegSessionID = _callLegSessionID;
    }

    /**
     * This method returns a callLegReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLeg value
     */
    public int getCallLegRefID() {
        return callLegRefID;
    }

    /**
     * This method returns a callLegSessionID.
     * 
     * @return a int value
     */
    public int getCallLegSessionID() {
        return callLegSessionID;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpCallLegIdentifier)) {
            return false;
        } 
        TpCallLegIdentifier tpCallLegIdentifier = (TpCallLegIdentifier) o;
        if(!(this.callLegSessionID == tpCallLegIdentifier.callLegSessionID)) {
            return false;
        }
        if(!(this.callLegRefID == tpCallLegIdentifier.callLegRefID))  {
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

} // TpCallLegIdentifier

