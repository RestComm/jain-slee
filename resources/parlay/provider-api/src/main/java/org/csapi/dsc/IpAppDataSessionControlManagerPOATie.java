package org.csapi.dsc;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppDataSessionControlManagerPOATie
	extends IpAppDataSessionControlManagerPOA
{
	private IpAppDataSessionControlManagerOperations _delegate;

	private POA _poa;
	public IpAppDataSessionControlManagerPOATie(IpAppDataSessionControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppDataSessionControlManagerPOATie(IpAppDataSessionControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.dsc.IpAppDataSessionControlManager _this()
	{
		return org.csapi.dsc.IpAppDataSessionControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.dsc.IpAppDataSessionControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.dsc.IpAppDataSessionControlManagerHelper.narrow(_this_object(orb));
	}
	public IpAppDataSessionControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppDataSessionControlManagerOperations delegate)
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
	public void dataSessionNotificationInterrupted()
	{
_delegate.dataSessionNotificationInterrupted();
	}

	public org.csapi.dsc.IpAppDataSession reportNotification(org.csapi.dsc.TpDataSessionIdentifier dataSessionReference, org.csapi.dsc.TpDataSessionEventInfo eventInfo, int assignmentID)
	{
		return _delegate.reportNotification(dataSessionReference,eventInfo,assignmentID);
	}

	public void dataSessionNotificationContinued()
	{
_delegate.dataSessionNotificationContinued();
	}

	public void dataSessionAborted(int dataSession)
	{
_delegate.dataSessionAborted(dataSession);
	}

}
