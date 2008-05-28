package org.csapi.cc.mmccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppMultiMediaCallPOATie
	extends IpAppMultiMediaCallPOA
{
	private IpAppMultiMediaCallOperations _delegate;

	private POA _poa;
	public IpAppMultiMediaCallPOATie(IpAppMultiMediaCallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppMultiMediaCallPOATie(IpAppMultiMediaCallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.mmccs.IpAppMultiMediaCall _this()
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpAppMultiMediaCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.narrow(_this_object(orb));
	}
	public IpAppMultiMediaCallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppMultiMediaCallOperations delegate)
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
	public void getInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.getInfoErr(callSessionID,errorIndication);
	}

	public void callEnded(int callSessionID, org.csapi.cc.TpCallEndedReport report)
	{
_delegate.callEnded(callSessionID,report);
	}

	public void superviseRes(int callSessionID, int report, int usedTime)
	{
_delegate.superviseRes(callSessionID,report,usedTime);
	}

	public void createAndRouteCallLegErr(int callSessionID, org.csapi.cc.mpccs.TpCallLegIdentifier callLegReference, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.createAndRouteCallLegErr(callSessionID,callLegReference,errorIndication);
	}

	public void superviseVolumeRes(int callSessionID, int report, org.csapi.cc.mmccs.TpCallSuperviseVolume usedVolume)
	{
_delegate.superviseVolumeRes(callSessionID,report,usedVolume);
	}

	public void getInfoRes(int callSessionID, org.csapi.cc.TpCallInfoReport callInfoReport)
	{
_delegate.getInfoRes(callSessionID,callInfoReport);
	}

	public void superviseErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.superviseErr(callSessionID,errorIndication);
	}

	public void superviseVolumeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
_delegate.superviseVolumeErr(callSessionID,errorIndication);
	}

}
