package org.csapi.cc.gccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpCallPOATie
	extends IpCallPOA
{
	private IpCallOperations _delegate;

	private POA _poa;
	public IpCallPOATie(IpCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpCallPOATie(IpCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.gccs.IpCall _this()
	{
		return org.csapi.cc.gccs.IpCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpCallHelper.narrow(_this_object(orb));
	}
	public IpCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpCallOperations delegate)
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

	public void getMoreDialledDigitsReq(int callSessionID, int length) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.getMoreDialledDigitsReq(callSessionID,length);
	}

	public void release(int callSessionID, org.csapi.cc.gccs.TpCallReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.release(callSessionID,cause);
	}

	public void setCallChargePlan(int callSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallChargePlan(callSessionID,callChargePlan);
	}

	public void superviseCallReq(int callSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseCallReq(callSessionID,time,treatment);
	}

	public void getCallInfoReq(int callSessionID, int callInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.getCallInfoReq(callSessionID,callInfoRequested);
	}

	public void continueProcessing(int callSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.continueProcessing(callSessionID);
	}

	public void deassignCall(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.deassignCall(callSessionID);
	}

	public int routeReq(int callSessionID, org.csapi.cc.gccs.TpCallReportRequest[] responseRequested, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.TpAddress originalDestinationAddress, org.csapi.TpAddress redirectingAddress, org.csapi.cc.gccs.TpCallAppInfo[] appInfo) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.routeReq(callSessionID,responseRequested,targetAddress,originatingAddress,originalDestinationAddress,redirectingAddress,appInfo);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void setAdviceOfCharge(int callSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setAdviceOfCharge(callSessionID,aOCInfo,tariffSwitch);
	}

}
