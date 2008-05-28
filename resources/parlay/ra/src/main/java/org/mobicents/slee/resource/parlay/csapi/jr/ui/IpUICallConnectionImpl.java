package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import javax.slee.resource.ResourceException;

import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.TpCommonExceptions;
import org.csapi.ui.P_ID_NOT_FOUND;
import org.csapi.ui.P_ILLEGAL_ID;
import org.csapi.ui.P_ILLEGAL_RANGE;
import org.csapi.ui.P_INVALID_COLLECTION_CRITERIA;
import org.csapi.ui.TpUICollectCriteria;
import org.csapi.ui.TpUIInfo;
import org.csapi.ui.TpUIMessageCriteria;
import org.csapi.ui.TpUIVariableInfo;
import org.mobicents.csapi.jr.slee.ui.IpUICallConnection;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICall;

/**
 *
 **/
/**
 * @author km0024
 * 
 */
public class IpUICallConnectionImpl implements IpUICallConnection {

    private final transient UICall ui;

    public IpUICallConnectionImpl(UICall ui) {
        super();
        if (ui == null) {
            throw new IllegalArgumentException("ui should never be null");
        }
        this.ui = ui;
    } 



    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws ResourceException {
        // do nothing
        // ui.closeConnection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#sendInfoReq(org.csapi.ui.TpUIInfo,
     *      java.lang.String, org.csapi.ui.TpUIVariableInfo[], int, int)
     */
    public int sendInfoReq(final TpUIInfo info, final String language,
            final TpUIVariableInfo[] variableInfo, final int repeatIndicator,
            final int responseRequested) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, P_ILLEGAL_ID, P_ID_NOT_FOUND,
            ResourceException {
        return ui.sendInfoReq(info, language, variableInfo, repeatIndicator,
                responseRequested);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#sendInfoAndCollectReq(org.csapi.ui.TpUIInfo,
     *      java.lang.String, org.csapi.ui.TpUIVariableInfo[],
     *      org.csapi.ui.TpUICollectCriteria, int)
     */
    public int sendInfoAndCollectReq(final TpUIInfo info, final String language,
            final TpUIVariableInfo[] variableInfo, final TpUICollectCriteria criteria,
            final int responseRequested) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, P_ILLEGAL_ID, P_ID_NOT_FOUND,
            P_ILLEGAL_RANGE, P_INVALID_COLLECTION_CRITERIA, ResourceException {
        return ui.sendInfoAndCollectReq(info, language, variableInfo, criteria,
                responseRequested);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#release()
     */
    public void release() throws TpCommonExceptions, ResourceException {
        ui.release();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#setOriginatingAddress(java.lang.String)
     */
    public void setOriginatingAddress(final String origin) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, P_INVALID_ADDRESS, ResourceException {
        ui.setOriginatingAddress(origin);

    }

    public String getOriginatingAddress() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {
        return ui.getOriginatingAddress();
    }

    public int recordMessageReq(final TpUIInfo info, final TpUIMessageCriteria criteria)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE, P_ILLEGAL_ID,
            P_ID_NOT_FOUND, P_INVALID_CRITERIA, ResourceException {
        return ui.recordMessageReq(info, criteria);
    }

    public int deleteMessageReq(final int messageID) throws TpCommonExceptions,
            P_ILLEGAL_ID, P_ID_NOT_FOUND, ResourceException {
        return ui.deleteMessageReq(messageID);
    }

    public void abortActionReq(final int assignmentID) throws TpCommonExceptions,
            P_INVALID_ASSIGNMENT_ID, ResourceException {
        ui.abortActionReq(assignmentID);

    }

}
