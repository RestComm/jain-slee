package org.csapi.termcap;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpTerminalCapabilitiesPOATie
	extends IpTerminalCapabilitiesPOA
{
	private IpTerminalCapabilitiesOperations _delegate;

	private POA _poa;
	public IpTerminalCapabilitiesPOATie(IpTerminalCapabilitiesOperations delegate)
	{
		_delegate = delegate;
	}
	public IpTerminalCapabilitiesPOATie(IpTerminalCapabilitiesOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.termcap.IpTerminalCapabilities _this()
	{
		return org.csapi.termcap.IpTerminalCapabilitiesHelper.narrow(_this_object());
	}
	public org.csapi.termcap.IpTerminalCapabilities _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.termcap.IpTerminalCapabilitiesHelper.narrow(_this_object(orb));
	}
	public IpTerminalCapabilitiesOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpTerminalCapabilitiesOperations delegate)
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

	public org.csapi.termcap.TpTerminalCapabilities getTerminalCapabilities(java.lang.String terminalIdentity) throws org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID
	{
		return _delegate.getTerminalCapabilities(terminalIdentity);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
