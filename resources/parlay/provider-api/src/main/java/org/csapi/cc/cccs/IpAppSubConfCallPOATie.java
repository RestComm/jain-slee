package org.csapi.cc.cccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppSubConfCallPOATie
	extends IpAppSubConfCallPOA
{
	private IpAppSubConfCallOperations _delegate;

	private POA _poa;
	public IpAppSubConfCallPOATie(IpAppSubConfCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppSubConfCallPOATie(IpAppSubConfCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.cccs.IpAppSubConfCall _this()
	{
		return org.csapi.cc.cccs.IpAppSubConfCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.cccs.IpAppSubConfCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.cccs.IpAppSubConfCallHelper.narrow(_this_object(orb));
	}
	public IpAppSubConfCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppSubConfCallOperations delegate)
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
	public void createAndRouteCallLegErr(int callSessionID, org.csapi.cc.mpccs.TpCallLegIdentifier callLegReference, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.createAndRouteCallLegErr(callSessionID,callLegReference,errorIndication);
	}

	public void superviseErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.superviseErr(callSessionID,errorIndication);
	}

	public void floorRequest(int subConferenceSessionID, int callLegSessionID)
	{
_delegate.floorRequest(subConferenceSessionID,callLegSessionID);
	}

	public void superviseRes(int callSessionID, int report, int usedTime)
	{
_delegate.superviseRes(callSessionID,report,usedTime);
	}

	public void getInfoRes(int callSessionID, org.csapi.cc.TpCallInfoReport callInfoReport)
	{
_delegate.getInfoRes(callSessionID,callInfoReport);
	}

	public void superviseVolumeRes(int callSessionID, int report, org.csapi.cc.mmccs.TpCallSuperviseVolume usedVolume)
	{
_delegate.superviseVolumeRes(callSessionID,report,usedVolume);
	}

	public void callEnded(int callSessionID, org.csapi.cc.TpCallEndedReport report)
	{
_delegate.callEnded(callSessionID,report);
	}

	public void getInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.getInfoErr(callSessionID,errorIndication);
	}

	public void superviseVolumeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.superviseVolumeErr(callSessionID,errorIndication);
	}

	public void chairSelection(int subConferenceSessionID, int callLegSessionID)
	{
_delegate.chairSelection(subConferenceSessionID,callLegSessionID);
	}

}
