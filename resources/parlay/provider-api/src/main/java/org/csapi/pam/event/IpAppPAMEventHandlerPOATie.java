package org.csapi.pam.event;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppPAMEventHandlerPOATie
	extends IpAppPAMEventHandlerPOA
{
	private IpAppPAMEventHandlerOperations _delegate;

	private POA _poa;
	public IpAppPAMEventHandlerPOATie(IpAppPAMEventHandlerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppPAMEventHandlerPOATie(IpAppPAMEventHandlerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.event.IpAppPAMEventHandler _this()
	{
		return org.csapi.pam.event.IpAppPAMEventHandlerHelper.narrow(_this_object());
	}
	public org.csapi.pam.event.IpAppPAMEventHandler _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.event.IpAppPAMEventHandlerHelper.narrow(_this_object(orb));
	}
	public IpAppPAMEventHandlerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppPAMEventHandlerOperations delegate)
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
	public void eventNotify(int eventID, org.csapi.pam.TpPAMNotificationInfo[] eventInfo)
	{
_delegate.eventNotify(eventID,eventInfo);
	}

	public void eventNotifyErr(int eventID, org.csapi.pam.TpPAMErrorInfo errorInfo)
	{
_delegate.eventNotifyErr(eventID,errorInfo);
	}

}
