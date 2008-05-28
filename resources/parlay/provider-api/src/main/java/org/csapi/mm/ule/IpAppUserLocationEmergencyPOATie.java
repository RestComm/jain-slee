package org.csapi.mm.ule;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUserLocationEmergencyPOATie
	extends IpAppUserLocationEmergencyPOA
{
	private IpAppUserLocationEmergencyOperations _delegate;

	private POA _poa;
	public IpAppUserLocationEmergencyPOATie(IpAppUserLocationEmergencyOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUserLocationEmergencyPOATie(IpAppUserLocationEmergencyOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ule.IpAppUserLocationEmergency _this()
	{
		return org.csapi.mm.ule.IpAppUserLocationEmergencyHelper.narrow(_this_object());
	}
	public org.csapi.mm.ule.IpAppUserLocationEmergency _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ule.IpAppUserLocationEmergencyHelper.narrow(_this_object(orb));
	}
	public IpAppUserLocationEmergencyOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUserLocationEmergencyOperations delegate)
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
	public void emergencyLocationReport(int assignmentId, org.csapi.mm.TpUserLocationEmergency location)
	{
_delegate.emergencyLocationReport(assignmentId,location);
	}

	public void emergencyLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.emergencyLocationReportErr(assignmentId,cause,diagnostic);
	}

}
