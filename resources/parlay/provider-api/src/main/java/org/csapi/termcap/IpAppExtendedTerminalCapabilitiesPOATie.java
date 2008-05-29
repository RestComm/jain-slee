package org.csapi.termcap;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppExtendedTerminalCapabilitiesPOATie
	extends IpAppExtendedTerminalCapabilitiesPOA
{
	private IpAppExtendedTerminalCapabilitiesOperations _delegate;

	private POA _poa;
	public IpAppExtendedTerminalCapabilitiesPOATie(IpAppExtendedTerminalCapabilitiesOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppExtendedTerminalCapabilitiesPOATie(IpAppExtendedTerminalCapabilitiesOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.termcap.IpAppExtendedTerminalCapabilities _this()
	{
		return org.csapi.termcap.IpAppExtendedTerminalCapabilitiesHelper.narrow(_this_object());
	}
	public org.csapi.termcap.IpAppExtendedTerminalCapabilities _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.termcap.IpAppExtendedTerminalCapabilitiesHelper.narrow(_this_object(orb));
	}
	public IpAppExtendedTerminalCapabilitiesOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppExtendedTerminalCapabilitiesOperations delegate)
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
	public void triggeredTerminalCapabilityReport(int assignmentID, org.csapi.TpAddress[] terminals, int criteria, org.csapi.termcap.TpTerminalCapabilities capabilities)
	{
_delegate.triggeredTerminalCapabilityReport(assignmentID,terminals,criteria,capabilities);
	}

	public void triggeredTerminalCapabilityReportErr(int assignmentId, org.csapi.TpAddress[] terminals, org.csapi.termcap.TpTerminalCapabilitiesError cause)
	{
_delegate.triggeredTerminalCapabilityReportErr(assignmentId,terminals,cause);
	}

}
