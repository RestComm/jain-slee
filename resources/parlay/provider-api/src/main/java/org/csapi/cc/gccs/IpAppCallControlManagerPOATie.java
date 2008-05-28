package org.csapi.cc.gccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppCallControlManagerPOATie
	extends IpAppCallControlManagerPOA
{
	private IpAppCallControlManagerOperations _delegate;

	private POA _poa;
	public IpAppCallControlManagerPOATie(IpAppCallControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppCallControlManagerPOATie(IpAppCallControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.gccs.IpAppCallControlManager _this()
	{
		return org.csapi.cc.gccs.IpAppCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpAppCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpAppCallControlManagerHelper.narrow(_this_object(orb));
	}
	public IpAppCallControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppCallControlManagerOperations delegate)
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
	public void callNotificationInterrupted()
	{
_delegate.callNotificationInterrupted();
	}

	public void callAborted(int callReference)
	{
_delegate.callAborted(callReference);
	}

	public void callOverloadCeased(int assignmentID)
	{
_delegate.callOverloadCeased(assignmentID);
	}

	public void callNotificationContinued()
	{
_delegate.callNotificationContinued();
	}

	public void callOverloadEncountered(int assignmentID)
	{
_delegate.callOverloadEncountered(assignmentID);
	}

	public org.csapi.cc.gccs.IpAppCall callEventNotify(org.csapi.cc.gccs.TpCallIdentifier callReference, org.csapi.cc.gccs.TpCallEventInfo eventInfo, int assignmentID)
	{
		return _delegate.callEventNotify(callReference,eventInfo,assignmentID);
	}

}
