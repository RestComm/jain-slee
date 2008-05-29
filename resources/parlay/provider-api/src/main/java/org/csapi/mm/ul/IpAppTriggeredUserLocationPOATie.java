package org.csapi.mm.ul;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppTriggeredUserLocationPOATie
	extends IpAppTriggeredUserLocationPOA
{
	private IpAppTriggeredUserLocationOperations _delegate;

	private POA _poa;
	public IpAppTriggeredUserLocationPOATie(IpAppTriggeredUserLocationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppTriggeredUserLocationPOATie(IpAppTriggeredUserLocationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ul.IpAppTriggeredUserLocation _this()
	{
		return org.csapi.mm.ul.IpAppTriggeredUserLocationHelper.narrow(_this_object());
	}
	public org.csapi.mm.ul.IpAppTriggeredUserLocation _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ul.IpAppTriggeredUserLocationHelper.narrow(_this_object(orb));
	}
	public IpAppTriggeredUserLocationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppTriggeredUserLocationOperations delegate)
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

	public void triggeredLocationReport(int assignmentId, org.csapi.mm.TpUserLocationExtended location, org.csapi.mm.TpLocationTriggerCriteria criterion)
	{
_delegate.triggeredLocationReport(assignmentId,location,criterion);
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

	public void triggeredLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.triggeredLocationReportErr(assignmentId,cause,diagnostic);
	}

	public void periodicLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.periodicLocationReportErr(assignmentId,cause,diagnostic);
	}

}
