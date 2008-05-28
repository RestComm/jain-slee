package org.csapi.mm.ulc;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUserLocationCamelPOATie
	extends IpAppUserLocationCamelPOA
{
	private IpAppUserLocationCamelOperations _delegate;

	private POA _poa;
	public IpAppUserLocationCamelPOATie(IpAppUserLocationCamelOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUserLocationCamelPOATie(IpAppUserLocationCamelOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ulc.IpAppUserLocationCamel _this()
	{
		return org.csapi.mm.ulc.IpAppUserLocationCamelHelper.narrow(_this_object());
	}
	public org.csapi.mm.ulc.IpAppUserLocationCamel _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ulc.IpAppUserLocationCamelHelper.narrow(_this_object(orb));
	}
	public IpAppUserLocationCamelOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUserLocationCamelOperations delegate)
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
	public void periodicLocationReport(int assignmentId, org.csapi.mm.TpUserLocationCamel[] locations)
	{
_delegate.periodicLocationReport(assignmentId,locations);
	}

	public void locationReportRes(int assignmentId, org.csapi.mm.TpUserLocationCamel[] locations)
	{
_delegate.locationReportRes(assignmentId,locations);
	}

	public void locationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.locationReportErr(assignmentId,cause,diagnostic);
	}

	public void triggeredLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.triggeredLocationReportErr(assignmentId,cause,diagnostic);
	}

	public void triggeredLocationReport(int assignmentId, org.csapi.mm.TpUserLocationCamel location, org.csapi.mm.TpLocationTriggerCamel criterion)
	{
_delegate.triggeredLocationReport(assignmentId,location,criterion);
	}

	public void periodicLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.periodicLocationReportErr(assignmentId,cause,diagnostic);
	}

}
