package org.csapi.dsc;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppDataSessionPOATie
	extends IpAppDataSessionPOA
{
	private IpAppDataSessionOperations _delegate;

	private POA _poa;
	public IpAppDataSessionPOATie(IpAppDataSessionOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppDataSessionPOATie(IpAppDataSessionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.dsc.IpAppDataSession _this()
	{
		return org.csapi.dsc.IpAppDataSessionHelper.narrow(_this_object());
	}
	public org.csapi.dsc.IpAppDataSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.dsc.IpAppDataSessionHelper.narrow(_this_object(orb));
	}
	public IpAppDataSessionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppDataSessionOperations delegate)
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
	public void dataSessionFaultDetected(int dataSessionID, org.csapi.dsc.TpDataSessionFault fault)
	{
_delegate.dataSessionFaultDetected(dataSessionID,fault);
	}

	public void superviseDataSessionErr(int dataSessionID, org.csapi.dsc.TpDataSessionError errorIndication)
	{
_delegate.superviseDataSessionErr(dataSessionID,errorIndication);
	}

	public void superviseDataSessionRes(int dataSessionID, int report, org.csapi.dsc.TpDataSessionSuperviseVolume usedVolume, org.csapi.TpDataSessionQosClass qualityOfService)
	{
_delegate.superviseDataSessionRes(dataSessionID,report,usedVolume,qualityOfService);
	}

	public void connectRes(int dataSessionID, org.csapi.dsc.TpDataSessionReport eventReport, int assignmentID)
	{
_delegate.connectRes(dataSessionID,eventReport,assignmentID);
	}

	public void connectErr(int dataSessionID, org.csapi.dsc.TpDataSessionError errorIndication, int assignmentID)
	{
_delegate.connectErr(dataSessionID,errorIndication,assignmentID);
	}

}
