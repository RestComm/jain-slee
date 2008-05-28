package org.csapi.cc.mpccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppMultiPartyCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppMultiPartyCallControlManagerPOATie
	extends IpAppMultiPartyCallControlManagerPOA
{
	private IpAppMultiPartyCallControlManagerOperations _delegate;

	private POA _poa;
	public IpAppMultiPartyCallControlManagerPOATie(IpAppMultiPartyCallControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppMultiPartyCallControlManagerPOATie(IpAppMultiPartyCallControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager _this()
	{
		return org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerHelper.narrow(_this_object(orb));
	}
	public IpAppMultiPartyCallControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppMultiPartyCallControlManagerOperations delegate)
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
	public void callAborted(int callReference)
	{
_delegate.callAborted(callReference);
	}

	public void callOverloadCeased(int assignmentID)
	{
_delegate.callOverloadCeased(assignmentID);
	}

	public void managerResumed()
	{
_delegate.managerResumed();
	}

	public void managerInterrupted()
	{
_delegate.managerInterrupted();
	}

	public void callOverloadEncountered(int assignmentID)
	{
_delegate.callOverloadEncountered(assignmentID);
	}

	public org.csapi.cc.mpccs.TpAppMultiPartyCallBack reportNotification(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier callReference, org.csapi.cc.mpccs.TpCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.TpCallNotificationInfo notificationInfo, int assignmentID)
	{
		return _delegate.reportNotification(callReference,callLegReferenceSet,notificationInfo,assignmentID);
	}

}
