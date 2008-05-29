package org.csapi.cc.gccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppCallPOATie
	extends IpAppCallPOA
{
	private IpAppCallOperations _delegate;

	private POA _poa;
	public IpAppCallPOATie(IpAppCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppCallPOATie(IpAppCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.gccs.IpAppCall _this()
	{
		return org.csapi.cc.gccs.IpAppCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpAppCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpAppCallHelper.narrow(_this_object(orb));
	}
	public IpAppCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppCallOperations delegate)
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
	public void callFaultDetected(int callSessionID, org.csapi.cc.gccs.TpCallFault fault)
	{
_delegate.callFaultDetected(callSessionID,fault);
	}

	public void superviseCallErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.superviseCallErr(callSessionID,errorIndication);
	}

	public void routeRes(int callSessionID, org.csapi.cc.gccs.TpCallReport eventReport, int callLegSessionID)
	{
_delegate.routeRes(callSessionID,eventReport,callLegSessionID);
	}

	public void getMoreDialledDigitsErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.getMoreDialledDigitsErr(callSessionID,errorIndication);
	}

	public void routeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication, int callLegSessionID)
	{
_delegate.routeErr(callSessionID,errorIndication,callLegSessionID);
	}

	public void callEnded(int callSessionID, org.csapi.cc.gccs.TpCallEndedReport report)
	{
_delegate.callEnded(callSessionID,report);
	}

	public void getCallInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.getCallInfoErr(callSessionID,errorIndication);
	}

	public void getCallInfoRes(int callSessionID, org.csapi.cc.gccs.TpCallInfoReport callInfoReport)
	{
_delegate.getCallInfoRes(callSessionID,callInfoReport);
	}

	public void getMoreDialledDigitsRes(int callSessionID, java.lang.String digits)
	{
_delegate.getMoreDialledDigitsRes(callSessionID,digits);
	}

	public void superviseCallRes(int callSessionID, int report, int usedTime)
	{
_delegate.superviseCallRes(callSessionID,report,usedTime);
	}

}
