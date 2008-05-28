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
import org.csapi.TpAoCInfo;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallChargePlan;
import org.csapi.cc.TpCallEventRequest;
import org.csapi.cc.TpReleaseCause;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;

/**
 */
public class IpMultiPartyCallConnectionImpl implements
        IpMultiPartyCallConnection {

    public IpMultiPartyCallConnectionImpl(MultiPartyCall multiPartyCall) {
        super();
        if (multiPartyCall == null) {
            throw new IllegalArgumentException(
                    "multiPartyCall should never be null");
        }
        this.multiPartyCall = multiPartyCall;
    }

    private final transient MultiPartyCall multiPartyCall;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#getIpCallLegConnection(org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier)
     */
    public IpCallLegConnection getIpCallLegConnection(
            final TpCallLegIdentifier callLegIdentifier)
            throws javax.slee.resource.ResourceException {

        return multiPartyCall.getIpCallLegConnection(callLegIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#getCallLegs()
     */
    public TpCallLegIdentifier[] getCallLegs() throws TpCommonExceptions,
            javax.slee.resource.ResourceException {
        return multiPartyCall.getCallLegs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#createCallLeg()
     */
    public TpCallLegIdentifier createCallLeg() throws TpCommonExceptions,
            javax.slee.resource.ResourceException {
        return multiPartyCall.createCallLeg();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#createAndRouteCallLegReq(org.csapi.cc.TpCallEventRequest[],
     *      org.csapi.TpAddress, org.csapi.TpAddress,
     *      org.csapi.cc.gccs.TpCallAppInfo[])
     */
    public TpCallLegIdentifier createAndRouteCallLegReq(
            final TpCallEventRequest[] eventsRequested, final TpAddress targetAddress,
            final TpAddress originatingAddress,
            final org.csapi.cc.TpCallAppInfo[] appInfo)
            throws TpCommonExceptions, P_INVALID_ADDRESS,
            P_UNSUPPORTED_ADDRESS_PLAN, P_INVALID_NETWORK_STATE,
            P_INVALID_EVENT_TYPE, P_INVALID_CRITERIA,
            javax.slee.resource.ResourceException {

        return multiPartyCall.createAndRouteCallLegReq(eventsRequested,
                targetAddress, originatingAddress, appInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#release(org.csapi.cc.TpReleaseCause)
     */
    public void release(final TpReleaseCause cause) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, javax.slee.resource.ResourceException {

        multiPartyCall.release(cause);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#deassignCall()
     */
    public void deassignCall() throws TpCommonExceptions,
            javax.slee.resource.ResourceException {

        multiPartyCall.deassignCall();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#getInfoReq(int)
     */
    public void getInfoReq(final int callInfoRequested) throws TpCommonExceptions,
            javax.slee.resource.ResourceException {

        multiPartyCall.getInfoReq(callInfoRequested);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#setChargePlan(org.csapi.cc.TpCallChargePlan)
     */
    public void setChargePlan(final TpCallChargePlan callChargePlan)
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        multiPartyCall.setChargePlan(callChargePlan);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#setAdviceOfCharge(org.csapi.TpAoCInfo,
     *      int)
     */
    public void setAdviceOfCharge(final TpAoCInfo aOCInfo, final int tariffSwitch)
            throws TpCommonExceptions, P_INVALID_CURRENCY, P_INVALID_AMOUNT,
            javax.slee.resource.ResourceException {

        multiPartyCall.setAdviceOfCharge(aOCInfo, tariffSwitch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#superviseReq(int,
     *      int)
     */
    public void superviseReq(final int time, final int treatment)
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        multiPartyCall.superviseReq(time, treatment);
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