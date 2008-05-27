
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs;

import javax.slee.resource.ResourceException;

import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddress;
import org.csapi.TpAoCInfo;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallChargePlan;
import org.csapi.cc.gccs.TpCallAppInfo;
import org.csapi.cc.gccs.TpCallReleaseCause;
import org.csapi.cc.gccs.TpCallReportRequest;
import org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;

/**
 *
 **/
public class IpCallConnectionImpl implements IpCallConnection {

    private final transient Call call;
    
    
    public IpCallConnectionImpl(Call call) {
        super();
        if (call == null) {
            throw new IllegalArgumentException(
                    "call should never be null");
        }
        this.call = call;
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#routeReq(org.csapi.cc.gccs.TpCallReportRequest[], org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.cc.gccs.TpCallAppInfo[])
     */
    public int routeReq(final TpCallReportRequest[] responseRequested, final TpAddress targetAddress, final TpAddress originatingAddress, final TpAddress originalDestinationAddress, final TpAddress redirectingAddress, final TpCallAppInfo[] appInfo) throws TpCommonExceptions, P_INVALID_ADDRESS, P_UNSUPPORTED_ADDRESS_PLAN, P_INVALID_NETWORK_STATE, P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE, ResourceException {
        
        return call.routeReq(responseRequested, targetAddress, originatingAddress, originalDestinationAddress, redirectingAddress, appInfo);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#release(org.csapi.cc.gccs.TpCallReleaseCause)
     */
    public void release(final TpCallReleaseCause cause) throws TpCommonExceptions, P_INVALID_NETWORK_STATE, ResourceException {
        call.release(cause);
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#deassignCall()
     */
    public void deassignCall() throws TpCommonExceptions, ResourceException {
        call.deassignCall();
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#getCallInfoReq(int)
     */
    public void getCallInfoReq(final int callInfoRequested) throws TpCommonExceptions, ResourceException {
        call.getCallInfoReq(callInfoRequested);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#setCallChargePlan(org.csapi.cc.TpCallChargePlan)
     */
    public void setCallChargePlan(final TpCallChargePlan callChargePlan) throws TpCommonExceptions, ResourceException {
        call.setCallChargePlan(callChargePlan);
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#setAdviceOfCharge(org.csapi.TpAoCInfo, int)
     */
    public void setAdviceOfCharge(final TpAoCInfo aOCInfo, final int tariffSwitch) throws TpCommonExceptions, ResourceException {
        call.setAdviceOfCharge(aOCInfo, tariffSwitch);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#getMoreDialledDigitsReq(int)
     */
    public void getMoreDialledDigitsReq(final int length) throws TpCommonExceptions, ResourceException {
        call.getMoreDialledDigitsReq(length);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#superviseCallReq(int, int)
     */
    public void superviseCallReq(final int time, final int treatment) throws TpCommonExceptions, ResourceException {
        call.superviseCallReq(time, treatment);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#continueProcessing()
     */
    public void continueProcessing() throws TpCommonExceptions, P_INVALID_NETWORK_STATE, ResourceException {
        call.continueProcessing();
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws ResourceException {
        //do nothing
        //call.closeConnection();
    }

}
