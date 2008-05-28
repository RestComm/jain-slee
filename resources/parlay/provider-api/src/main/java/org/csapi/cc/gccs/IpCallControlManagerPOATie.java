package org.csapi.cc.gccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpCallControlManagerPOATie
	extends IpCallControlManagerPOA
{
	private IpCallControlManagerOperations _delegate;

	private POA _poa;
	public IpCallControlManagerPOATie(IpCallControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpCallControlManagerPOATie(IpCallControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.gccs.IpCallControlManager _this()
	{
		return org.csapi.cc.gccs.IpCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpCallControlManagerHelper.narrow(_this_object(orb));
	}
	public IpCallControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpCallControlManagerOperations delegate)
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
	public void disableCallNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.disableCallNotification(assignmentID);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public int setCallLoadControl(int duration, org.csapi.cc.TpCallLoadControlMechanism mechanism, org.csapi.cc.gccs.TpCallTreatment treatment, org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN
	{
		return _delegate.setCallLoadControl(duration,mechanism,treatment,addressRange);
	}

	public void changeCallNotification(int assignmentID, org.csapi.cc.gccs.TpCallEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeCallNotification(assignmentID,eventCriteria);
	}

	public org.csapi.cc.gccs.TpCallIdentifier createCall(org.csapi.cc.gccs.IpAppCall appCall) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		return _delegate.createCall(appCall);
	}

	public org.csapi.cc.gccs.TpCallEventCriteriaResult[] getCriteria() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getCriteria();
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public int enableCallNotification(org.csapi.cc.gccs.IpAppCallControlManager appCallControlManager, org.csapi.cc.gccs.TpCallEventCriteria eventCriteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.enableCallNotification(appCallControlManager,eventCriteria);
	}

}
