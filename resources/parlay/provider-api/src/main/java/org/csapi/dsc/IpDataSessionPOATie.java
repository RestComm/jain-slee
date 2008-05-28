package org.csapi.dsc;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpDataSessionPOATie
	extends IpDataSessionPOA
{
	private IpDataSessionOperations _delegate;

	private POA _poa;
	public IpDataSessionPOATie(IpDataSessionOperations delegate)
	{
		_delegate = delegate;
	}
	public IpDataSessionPOATie(IpDataSessionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.dsc.IpDataSession _this()
	{
		return org.csapi.dsc.IpDataSessionHelper.narrow(_this_object());
	}
	public org.csapi.dsc.IpDataSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.dsc.IpDataSessionHelper.narrow(_this_object(orb));
	}
	public IpDataSessionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpDataSessionOperations delegate)
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

	public void release(int dataSessionID, org.csapi.dsc.TpDataSessionReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.release(dataSessionID,cause);
	}

	public void deassignDataSession(int dataSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.deassignDataSession(dataSessionID);
	}

	public void setDataSessionChargePlan(int dataSessionID, org.csapi.dsc.TpDataSessionChargePlan dataSessionChargePlan) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setDataSessionChargePlan(dataSessionID,dataSessionChargePlan);
	}

	public void superviseDataSessionReq(int dataSessionID, int treatment, org.csapi.dsc.TpDataSessionSuperviseVolume bytes) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.superviseDataSessionReq(dataSessionID,treatment,bytes);
	}

	public void continueProcessing(int dataSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.continueProcessing(dataSessionID);
	}

	public int connectReq(int dataSessionID, org.csapi.dsc.TpDataSessionReportRequest[] responseRequested, org.csapi.TpAddress targetAddress) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.connectReq(dataSessionID,responseRequested,targetAddress);
	}

	public void setAdviceOfCharge(int dataSessionID, org.csapi.TpAoCInfo aoCInfo, int tariffSwitch) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT
	{
_delegate.setAdviceOfCharge(dataSessionID,aoCInfo,tariffSwitch);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

}
