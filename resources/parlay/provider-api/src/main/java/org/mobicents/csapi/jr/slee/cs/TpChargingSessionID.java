package org.mobicents.csapi.jr.slee.cs;

/**
 * Defines the Sequence of Data Elements that unambiguously specify the Charging Session object.
 * 
 */
public final class TpChargingSessionID implements java.io.Serializable {

    private int chargingSessionRefID;
    private int chargingSessionID;
    private int requestNumberFirstRequest;


    /**
     * Creates a new TpChargingSessionID instance.
     * @param _chargingSessionRefID   This element specifies the interface reference for the charging session object.
     * @param _chargingSessionID   This element specifies the session ID for the charging session.
     * @param _requestNumberFirstRequest   This element specifies the request number to use for the next request.
     */
    public TpChargingSessionID (int _chargingSessionRefID, int _chargingSessionID, int _requestNumberFirstRequest) {

        this.chargingSessionRefID = _chargingSessionRefID;
        this.chargingSessionID = _chargingSessionID;
        this.requestNumberFirstRequest = _requestNumberFirstRequest;
    }

    /**
     * This method returns a chargingSessionReference.
     * 
     * @return a org.mobicents.csapi.jr.slee.cs.IpChargingSession value
     */
    public int getChargingSessionRefID() {
        return chargingSessionRefID;
    }

    /**
     * This method returns a chargingSessionID.
     * 
     * @return a int value
     */
    public int getChargingSessionID() {
        return chargingSessionID;
    }

    /**
     * This method returns a requestNumberFirstRequest.
     * 
     * @return a int value
     */
    public int getRequestNumberFirstRequest() {
        return requestNumberFirstRequest;
    }



    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TpChargingSessionID)) {
            return false;
        } 
        TpChargingSessionID tpChargingSessionID = (TpChargingSessionID) o;
        if(!(this.chargingSessionID == tpChargingSessionID.chargingSessionID)) {
            return false;
        }
        if(!(this.requestNumberFirstRequest == tpChargingSessionID.requestNumberFirstRequest)) {
            return false;
        }
        if(!(this.chargingSessionRefID == tpChargingSessionID.chargingSessionRefID))  {
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

} // TpChargingSessionID

