package org.csapi.cs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpChargingManagerPOATie
	extends IpChargingManagerPOA
{
	private IpChargingManagerOperations _delegate;

	private POA _poa;
	public IpChargingManagerPOATie(IpChargingManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpChargingManagerPOATie(IpChargingManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cs.IpChargingManager _this()
	{
		return org.csapi.cs.IpChargingManagerHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpChargingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpChargingManagerHelper.narrow(_this_object(orb));
	}
	public IpChargingManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpChargingManagerOperations delegate)
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
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public org.csapi.cs.TpChargingSessionID createSplitChargingSession(org.csapi.cs.IpAppChargingSession appChargingSession, java.lang.String sessionDescription, org.csapi.cs.TpMerchantAccountID merchantAccount, org.csapi.TpAddress[] users, org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_ACCOUNT,org.csapi.cs.P_INVALID_USER
	{
		return _delegate.createSplitChargingSession(appChargingSession,sessionDescription,merchantAccount,users,correlationID);
	}

	public org.csapi.cs.TpChargingSessionID createChargingSession(org.csapi.cs.IpAppChargingSession appChargingSession, java.lang.String sessionDescription, org.csapi.cs.TpMerchantAccountID merchantAccount, org.csapi.TpAddress user, org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_ACCOUNT,org.csapi.cs.P_INVALID_USER
	{
		return _delegate.createChargingSession(appChargingSession,sessionDescription,merchantAccount,user,correlationID);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
