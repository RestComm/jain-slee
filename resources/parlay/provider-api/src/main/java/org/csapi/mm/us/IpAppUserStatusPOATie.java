package org.csapi.mm.us;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUserStatusPOATie
	extends IpAppUserStatusPOA
{
	private IpAppUserStatusOperations _delegate;

	private POA _poa;
	public IpAppUserStatusPOATie(IpAppUserStatusOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUserStatusPOATie(IpAppUserStatusOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.us.IpAppUserStatus _this()
	{
		return org.csapi.mm.us.IpAppUserStatusHelper.narrow(_this_object());
	}
	public org.csapi.mm.us.IpAppUserStatus _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.us.IpAppUserStatusHelper.narrow(_this_object(orb));
	}
	public IpAppUserStatusOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUserStatusOperations delegate)
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
	public void statusReportRes(int assignmentId, org.csapi.mm.TpUserStatus[] status)
	{
_delegate.statusReportRes(assignmentId,status);
	}

	public void triggeredStatusReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.triggeredStatusReportErr(assignmentId,cause,diagnostic);
	}

	public void triggeredStatusReport(int assignmentId, org.csapi.mm.TpUserStatus status)
	{
_delegate.triggeredStatusReport(assignmentId,status);
	}

	public void statusReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
_delegate.statusReportErr(assignmentId,cause,diagnostic);
	}

}
