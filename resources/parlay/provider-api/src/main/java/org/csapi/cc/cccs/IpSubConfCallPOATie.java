package org.csapi.cc.cccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSubConfCallPOATie
	extends IpSubConfCallPOA
{
	private IpSubConfCallOperations _delegate;

	private POA _poa;
	public IpSubConfCallPOATie(IpSubConfCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSubConfCallPOATie(IpSubConfCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.cccs.IpSubConfCall _this()
	{
		return org.csapi.cc.cccs.IpSubConfCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.cccs.IpSubConfCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.cccs.IpSubConfCallHelper.narrow(_this_object(orb));
	}
	public IpSubConfCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSubConfCallOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public void chairSelection(int subConferenceSessionID, int chairCallLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.chairSelection(subConferenceSessionID,chairCallLeg);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public void changeConferencePolicy(int subConferenceSessionID, org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.changeConferencePolicy(subConferenceSessionID,conferencePolicy);
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier createCallLeg(int callSessionID, org.csapi.cc.mpccs.IpAppCallLeg appCallLeg) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.createCallLeg(callSessionID,appCallLeg);
	}

	public void mergeSubConference(int subConferenceCallSessionID, int targetSubConferenceCall) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.mergeSubConference(subConferenceCallSessionID,targetSubConferenceCall);
	}

	public void release(int callSessionID, org.csapi.cc.TpReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.release(callSessionID,cause);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void deassignCall(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.deassignCall(callSessionID);
	}

	public void setChargePlan(int callSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setChargePlan(callSessionID,callChargePlan);
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier[] getCallLegs(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getCallLegs(callSessionID);
	}

	public void appointSpeaker(int subConferenceSessionID, int speakerCallLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.appointSpeaker(subConferenceSessionID,speakerCallLeg);
	}

	public org.csapi.cc.cccs.TpSubConfCallIdentifier splitSubConference(int subConferenceSessionID, int[] callLegList, org.csapi.cc.cccs.IpAppSubConfCall appSubConferenceCall) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.splitSubConference(subConferenceSessionID,callLegList,appSubConferenceCall);
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier createAndRouteCallLegReq(int callSessionID, org.csapi.cc.TpCallEventRequest[] eventsRequested, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.cc.TpCallAppInfo[] appInfo, org.csapi.cc.mpccs.IpAppCallLeg appLegInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createAndRouteCallLegReq(callSessionID,eventsRequested,targetAddress,originatingAddress,appInfo,appLegInterface);
	}

	public void inspectVideoCancel(int subConferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.inspectVideoCancel(subConferenceSessionID);
	}

	public void moveCallLeg(int subConferenceCallSessionID, int targetSubConferenceCall, int callLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.moveCallLeg(subConferenceCallSessionID,targetSubConferenceCall,callLeg);
	}

	public void inspectVideo(int subConferenceSessionID, int inspectedCallLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.inspectVideo(subConferenceSessionID,inspectedCallLeg);
	}

	public void superviseVolumeReq(int callSessionID, org.csapi.cc.mmccs.TpCallSuperviseVolume volume, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseVolumeReq(callSessionID,volume,treatment);
	}

	public void setAdviceOfCharge(int callSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setAdviceOfCharge(callSessionID,aOCInfo,tariffSwitch);
	}

	public void superviseReq(int callSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseReq(callSessionID,time,treatment);
	}

	public void getInfoReq(int callSessionID, int callInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.getInfoReq(callSessionID,callInfoRequested);
	}

}
