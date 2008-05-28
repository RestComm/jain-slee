package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import javax.slee.resource.ResourceException;

import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_AMOUNT;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_CURRENCY;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddress;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallAppInfo;
import org.csapi.cc.TpCallLegConnectionProperties;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;

/**
 */
public class IpCallLegConnectionImpl implements IpCallLegConnection {

    public IpCallLegConnectionImpl(CallLeg callLeg) {
        super();
        if (callLeg == null) {
            throw new IllegalArgumentException("callLeg should never be null");
        }
        this.callLeg = callLeg;
    }

    private final transient CallLeg callLeg;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#routeReq(org.csapi.TpAddress,
     *      org.csapi.TpAddress, org.csapi.cc.gccs.TpCallAppInfo[],
     *      org.csapi.cc.TpCallLegConnectionProperties)
     */
    public void routeReq(final TpAddress targetAddress, final TpAddress originatingAddress,
            final TpCallAppInfo[] appInfo,
            final TpCallLegConnectionProperties connectionProperties)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE,
            P_INVALID_ADDRESS, P_UNSUPPORTED_ADDRESS_PLAN, ResourceException {
        
        callLeg.routeReq(targetAddress, originatingAddress, appInfo,
                connectionProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#eventReportReq(org.csapi.cc.TpCallEventRequest[])
     */
    public void eventReportReq(final org.csapi.cc.TpCallEventRequest[] eventsRequested)
            throws TpCommonExceptions, P_INVALID_EVENT_TYPE,
            P_INVALID_CRITERIA, javax.slee.resource.ResourceException {

        callLeg.eventReportReq(eventsRequested);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#release(org.csapi.cc.TpReleaseCause)
     */
    public void release(final org.csapi.cc.TpReleaseCause cause)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE,
            javax.slee.resource.ResourceException {

        callLeg.release(cause);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#getInfoReq(int)
     */
    public void getInfoReq(final int callLegInfoRequested) throws TpCommonExceptions,
            javax.slee.resource.ResourceException {

        callLeg.getInfoReq(callLegInfoRequested);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#getCall()
     */
    public TpMultiPartyCallIdentifier getCall() throws TpCommonExceptions,
            javax.slee.resource.ResourceException {

        return callLeg.getCall();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#attachMediaReq()
     */
    public void attachMediaReq() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, javax.slee.resource.ResourceException {

        callLeg.attachMediaReq();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#detachMediaReq()
     */
    public void detachMediaReq() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, javax.slee.resource.ResourceException {

        callLeg.detachMediaReq();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#getCurrentDestinationAddress()
     */
    public org.csapi.TpAddress getCurrentDestinationAddress()
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        return callLeg.getCurrentDestinationAddress();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#continueProcessing()
     */
    public void continueProcessing() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, javax.slee.resource.ResourceException {

        callLeg.continueProcessing();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#setChargePlan(org.csapi.cc.TpCallChargePlan)
     */
    public void setChargePlan(final org.csapi.cc.TpCallChargePlan callChargePlan)
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        callLeg.setChargePlan(callChargePlan);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#setAdviceOfCharge(org.csapi.TpAoCInfo,
     *      int)
     */
    public void setAdviceOfCharge(final org.csapi.TpAoCInfo aOCInfo, final int tariffSwitch)
            throws TpCommonExceptions, P_INVALID_CURRENCY, P_INVALID_AMOUNT,
            javax.slee.resource.ResourceException {

        callLeg.setAdviceOfCharge(aOCInfo, tariffSwitch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#superviseReq(int,
     *      int)
     */
    public void superviseReq(final int time, final int treatment)
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        callLeg.superviseReq(time, treatment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#deassign()
     */
    public void deassign() throws TpCommonExceptions,
            javax.slee.resource.ResourceException {

        callLeg.deassign();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#close()
     */
    public void closeConnection() throws ResourceException {
        // Do nothing
    }
}