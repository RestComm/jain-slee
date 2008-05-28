package org.csapi.fw.fw_service.notification;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSvcEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSvcEventNotificationPOATie
	extends IpSvcEventNotificationPOA
{
	private IpSvcEventNotificationOperations _delegate;

	private POA _poa;
	public IpSvcEventNotificationPOATie(IpSvcEventNotificationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSvcEventNotificationPOATie(IpSvcEventNotificationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.notification.IpSvcEventNotification _this()
	{
		return org.csapi.fw.fw_service.notification.IpSvcEventNotificationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.notification.IpSvcEventNotification _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.notification.IpSvcEventNotificationHelper.narrow(_this_object(orb));
	}
	public IpSvcEventNotificationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSvcEventNotificationOperations delegate)
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
	public void reportNotification(org.csapi.fw.TpFwEventInfo eventInfo, int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.reportNotification(eventInfo,assignmentID);
	}

	public void notificationTerminated() throws org.csapi.TpCommonExceptions
	{
_delegate.notificationTerminated();
	}

}
