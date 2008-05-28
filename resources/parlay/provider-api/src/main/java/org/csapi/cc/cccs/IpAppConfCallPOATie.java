package org.csapi.cc.cccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppConfCallPOATie
	extends IpAppConfCallPOA
{
	private IpAppConfCallOperations _delegate;

	private POA _poa;
	public IpAppConfCallPOATie(IpAppConfCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppConfCallPOATie(IpAppConfCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.cccs.IpAppConfCall _this()
	{
		return org.csapi.cc.cccs.IpAppConfCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.cccs.IpAppConfCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.cccs.IpAppConfCallHelper.narrow(_this_object(orb));
	}
	public IpAppConfCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppConfCallOperations delegate)
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

	public void leaveMonitorRes(int conferenceSessionID, int callLeg)
	{
_delegate.leaveMonitorRes(conferenceSessionID,callLeg);
	}

	public org.csapi.cc.mpccs.IpAppCallLeg partyJoined(int conferenceSessionID, org.csapi.cc.mpccs.TpCallLegIdentifier callLeg, org.csapi.cc.cccs.TpJoinEventInfo eventInfo)
	{
		return _delegate.partyJoined(conferenceSessionID,callLeg,eventInfo);
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

}
