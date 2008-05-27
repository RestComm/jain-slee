package org.csapi.cc.mmccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMultiMediaCallLegPOATie
	extends IpMultiMediaCallLegPOA
{
	private IpMultiMediaCallLegOperations _delegate;

	private POA _poa;
	public IpMultiMediaCallLegPOATie(IpMultiMediaCallLegOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMultiMediaCallLegPOATie(IpMultiMediaCallLegOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.mmccs.IpMultiMediaCallLeg _this()
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallLegHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpMultiMediaCallLeg _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallLegHelper.narrow(_this_object(orb));
	}
	public IpMultiMediaCallLegOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMultiMediaCallLegOperations delegate)
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
	public void routeReq(int callLegSessionID, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.cc.TpCallAppInfo[] appInfo, org.csapi.cc.TpCallLegConnectionProperties connectionProperties) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN
	{
_delegate.routeReq(callLegSessionID,targetAddress,originatingAddress,appInfo,connectionProperties);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public void detachMediaReq(int callLegSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.detachMediaReq(callLegSessionID);
	}

	public void release(int callLegSessionID, org.csapi.cc.TpReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.release(callLegSessionID,cause);
	}

	public void deassign(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.deassign(callLegSessionID);
	}

	public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier getCall(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getCall(callLegSessionID);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void setChargePlan(int callLegSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setChargePlan(callLegSessionID,callChargePlan);
	}

	public void attachMediaReq(int callLegSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.attachMediaReq(callLegSessionID);
	}

	public org.csapi.cc.mmccs.TpMediaStream[] getMediaStreams(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getMediaStreams(callLegSessionID);
	}

	public void mediaStreamAllow(int callLegSessionID, int[] mediaStreamList) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.mediaStreamAllow(callLegSessionID,mediaStreamList);
	}

	public void setAdviceOfCharge(int callLegSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setAdviceOfCharge(callLegSessionID,aOCInfo,tariffSwitch);
	}

	public void eventReportReq(int callLegSessionID, org.csapi.cc.TpCallEventRequest[] eventsRequested) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_INVALID_CRITERIA
	{
_delegate.eventReportReq(callLegSessionID,eventsRequested);
	}

	public void continueProcessing(int callLegSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.continueProcessing(callLegSessionID);
	}

	public void superviseReq(int callLegSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseReq(callLegSessionID,time,treatment);
	}

	public void getInfoReq(int callLegSessionID, int callLegInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.getInfoReq(callLegSessionID,callLegInfoRequested);
	}

	public org.csapi.TpAddress getCurrentDestinationAddress(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getCurrentDestinationAddress(callLegSessionID);
	}

	public void mediaStreamMonitorReq(int callLegSessionID, org.csapi.cc.mmccs.TpMediaStreamRequest[] mediaStreamEventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_INVALID_CRITERIA
	{
_delegate.mediaStreamMonitorReq(callLegSessionID,mediaStreamEventCriteria);
	}

}
