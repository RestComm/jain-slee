package org.csapi.cc.mmccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppMultiMediaCallLegPOATie
	extends IpAppMultiMediaCallLegPOA
{
	private IpAppMultiMediaCallLegOperations _delegate;

	private POA _poa;
	public IpAppMultiMediaCallLegPOATie(IpAppMultiMediaCallLegOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppMultiMediaCallLegPOATie(IpAppMultiMediaCallLegOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.mmccs.IpAppMultiMediaCallLeg _this()
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallLegHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpAppMultiMediaCallLeg _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallLegHelper.narrow(_this_object(orb));
	}
	public IpAppMultiMediaCallLegOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppMultiMediaCallLegOperations delegate)
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
	public void attachMediaRes(int callLegSessionID)
	{
_delegate.attachMediaRes(callLegSessionID);
	}

	public void routeErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.routeErr(callLegSessionID,errorIndication);
	}

	public void superviseErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.superviseErr(callLegSessionID,errorIndication);
	}

	public void mediaStreamMonitorRes(int callLegSessionID, org.csapi.cc.mmccs.TpMediaStream[] streams, org.csapi.cc.mmccs.TpMediaStreamEventType type)
	{
_delegate.mediaStreamMonitorRes(callLegSessionID,streams,type);
	}

	public void eventReportRes(int callLegSessionID, org.csapi.cc.TpCallEventInfo eventInfo)
	{
_delegate.eventReportRes(callLegSessionID,eventInfo);
	}

	public void attachMediaErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.attachMediaErr(callLegSessionID,errorIndication);
	}

	public void superviseRes(int callLegSessionID, int report, int usedTime)
	{
_delegate.superviseRes(callLegSessionID,report,usedTime);
	}

	public void eventReportErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.eventReportErr(callLegSessionID,errorIndication);
	}

	public void detachMediaRes(int callLegSessionID)
	{
_delegate.detachMediaRes(callLegSessionID);
	}

	public void detachMediaErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.detachMediaErr(callLegSessionID,errorIndication);
	}

	public void callLegEnded(int callLegSessionID, org.csapi.cc.TpReleaseCause cause)
	{
_delegate.callLegEnded(callLegSessionID,cause);
	}

	public void getInfoErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.getInfoErr(callLegSessionID,errorIndication);
	}

	public void getInfoRes(int callLegSessionID, org.csapi.cc.TpCallLegInfoReport callLegInfoReport)
	{
_delegate.getInfoRes(callLegSessionID,callLegInfoReport);
	}

}
