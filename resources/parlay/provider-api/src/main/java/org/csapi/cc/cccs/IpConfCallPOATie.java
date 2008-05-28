package org.csapi.cc.cccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpConfCallPOATie
	extends IpConfCallPOA
{
	private IpConfCallOperations _delegate;

	private POA _poa;
	public IpConfCallPOATie(IpConfCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpConfCallPOATie(IpConfCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.cccs.IpConfCall _this()
	{
		return org.csapi.cc.cccs.IpConfCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.cccs.IpConfCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.cccs.IpConfCallHelper.narrow(_this_object(orb));
	}
	public IpConfCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpConfCallOperations delegate)
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
	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier[] getCallLegs(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getCallLegs(callSessionID);
	}

	public org.csapi.cc.cccs.TpSubConfCallIdentifier[] getSubConferences(int conferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getSubConferences(conferenceSessionID);
	}

	public org.csapi.cc.cccs.TpSubConfCallIdentifier createSubConference(int conferenceSessionID, org.csapi.cc.cccs.IpAppSubConfCall appSubConference, org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.createSubConference(conferenceSessionID,appSubConference,conferencePolicy);
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier createAndRouteCallLegReq(int callSessionID, org.csapi.cc.TpCallEventRequest[] eventsRequested, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.cc.TpCallAppInfo[] appInfo, org.csapi.cc.mpccs.IpAppCallLeg appLegInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createAndRouteCallLegReq(callSessionID,eventsRequested,targetAddress,originatingAddress,appInfo,appLegInterface);
	}

	public void superviseVolumeReq(int callSessionID, org.csapi.cc.mmccs.TpCallSuperviseVolume volume, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseVolumeReq(callSessionID,volume,treatment);
	}

	public void getInfoReq(int callSessionID, int callInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.getInfoReq(callSessionID,callInfoRequested);
	}

	public org.csapi.TpAddress getConferenceAddress(int conferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getConferenceAddress(conferenceSessionID);
	}

	public org.csapi.cc.mpccs.TpCallLegIdentifier createCallLeg(int callSessionID, org.csapi.cc.mpccs.IpAppCallLeg appCallLeg) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.createCallLeg(callSessionID,appCallLeg);
	}

	public void deassignCall(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.deassignCall(callSessionID);
	}

	public void release(int callSessionID, org.csapi.cc.TpReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.release(callSessionID,cause);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void setAdviceOfCharge(int callSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setAdviceOfCharge(callSessionID,aOCInfo,tariffSwitch);
	}

	public void setChargePlan(int callSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setChargePlan(callSessionID,callChargePlan);
	}

	public void leaveMonitorReq(int conferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.leaveMonitorReq(conferenceSessionID);
	}

	public void superviseReq(int callSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseReq(callSessionID,time,treatment);
	}

}
