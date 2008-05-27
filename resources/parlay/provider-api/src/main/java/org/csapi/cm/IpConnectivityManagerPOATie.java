package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpConnectivityManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpConnectivityManagerPOATie
	extends IpConnectivityManagerPOA
{
	private IpConnectivityManagerOperations _delegate;

	private POA _poa;
	public IpConnectivityManagerPOATie(IpConnectivityManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpConnectivityManagerPOATie(IpConnectivityManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpConnectivityManager _this()
	{
		return org.csapi.cm.IpConnectivityManagerHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpConnectivityManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpConnectivityManagerHelper.narrow(_this_object(orb));
	}
	public IpConnectivityManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpConnectivityManagerOperations delegate)
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

	public org.csapi.IpInterface getEnterpriseNetwork() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORK
	{
		return _delegate.getEnterpriseNetwork();
	}

	public org.csapi.IpInterface getQoSMenu() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_MENU
	{
		return _delegate.getQoSMenu();
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
