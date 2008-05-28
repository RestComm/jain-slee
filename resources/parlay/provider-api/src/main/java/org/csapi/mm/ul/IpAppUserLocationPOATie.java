package org.csapi.mm.ul;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUserLocationPOATie
	extends IpAppUserLocationPOA
{
	private IpAppUserLocationOperations _delegate;

	private POA _poa;
	public IpAppUserLocationPOATie(IpAppUserLocationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUserLocationPOATie(IpAppUserLocationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ul.IpAppUserLocation _this()
	{
		return org.csapi.mm.ul.IpAppUserLocationHelper.narrow(_this_object());
	}
	public org.csapi.mm.ul.IpAppUserLocation _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ul.IpAppUserLocationHelper.narrow(_this_object(orb));
	}
	public IpAppUserLocationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUserLocationOperations delegate)
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
	public void locationReportRes(int assignmentId, org.csapi.mm.TpUserLocation[] locations)
	{
_delegate.locationReportRes(assignmentId,locations);
	}

	public void extendedLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.extendedLocationReportErr(assignmentId,cause,diagnostic);
	}

	public void periodicLocationReport(int assignmentId, org.csapi.mm.TpUserLocationExtended[] locations)
	{
_delegate.periodicLocationReport(assignmentId,locations);
	}

	public void extendedLocationReportRes(int assignmentId, org.csapi.mm.TpUserLocationExtended[] locations)
	{
_delegate.extendedLocationReportRes(assignmentId,locations);
	}

	public void locationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.locationReportErr(assignmentId,cause,diagnostic);
	}

	public void periodicLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.periodicLocationReportErr(assignmentId,cause,diagnostic);
	}

}
