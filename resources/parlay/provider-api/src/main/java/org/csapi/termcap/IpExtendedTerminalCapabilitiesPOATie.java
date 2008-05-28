package org.csapi.termcap;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpExtendedTerminalCapabilitiesPOATie
	extends IpExtendedTerminalCapabilitiesPOA
{
	private IpExtendedTerminalCapabilitiesOperations _delegate;

	private POA _poa;
	public IpExtendedTerminalCapabilitiesPOATie(IpExtendedTerminalCapabilitiesOperations delegate)
	{
		_delegate = delegate;
	}
	public IpExtendedTerminalCapabilitiesPOATie(IpExtendedTerminalCapabilitiesOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.termcap.IpExtendedTerminalCapabilities _this()
	{
		return org.csapi.termcap.IpExtendedTerminalCapabilitiesHelper.narrow(_this_object());
	}
	public org.csapi.termcap.IpExtendedTerminalCapabilities _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.termcap.IpExtendedTerminalCapabilitiesHelper.narrow(_this_object(orb));
	}
	public IpExtendedTerminalCapabilitiesOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpExtendedTerminalCapabilitiesOperations delegate)
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

	public int triggeredTerminalCapabilityStartReq(org.csapi.termcap.IpAppExtendedTerminalCapabilities appTerminalCapabilities, org.csapi.TpAddress[] terminals, org.csapi.termcap.TpTerminalCapabilityScope capabilityScope, int criteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.triggeredTerminalCapabilityStartReq(appTerminalCapabilities,terminals,capabilityScope,criteria);
	}

	public org.csapi.termcap.TpTerminalCapabilities getTerminalCapabilities(java.lang.String terminalIdentity) throws org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID
	{
		return _delegate.getTerminalCapabilities(terminalIdentity);
	}

	public void triggeredTerminalCapabilityStop(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.triggeredTerminalCapabilityStop(assignmentID);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
