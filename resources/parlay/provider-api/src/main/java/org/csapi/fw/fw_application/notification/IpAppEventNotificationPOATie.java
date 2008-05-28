package org.csapi.fw.fw_application.notification;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppEventNotificationPOATie
	extends IpAppEventNotificationPOA
{
	private IpAppEventNotificationOperations _delegate;

	private POA _poa;
	public IpAppEventNotificationPOATie(IpAppEventNotificationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppEventNotificationPOATie(IpAppEventNotificationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.notification.IpAppEventNotification _this()
	{
		return org.csapi.fw.fw_application.notification.IpAppEventNotificationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.notification.IpAppEventNotification _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.notification.IpAppEventNotificationHelper.narrow(_this_object(orb));
	}
	public IpAppEventNotificationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppEventNotificationOperations delegate)
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
	public void reportNotification(org.csapi.fw.TpFwEventInfo eventInfo, int assignmentID)
	{
_delegate.reportNotification(eventInfo,assignmentID);
	}

	public void notificationTerminated()
	{
_delegate.notificationTerminated();
	}

}
