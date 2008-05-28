package org.csapi.fw.fw_application.notification;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpEventNotificationPOATie
	extends IpEventNotificationPOA
{
	private IpEventNotificationOperations _delegate;

	private POA _poa;
	public IpEventNotificationPOATie(IpEventNotificationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpEventNotificationPOATie(IpEventNotificationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.notification.IpEventNotification _this()
	{
		return org.csapi.fw.fw_application.notification.IpEventNotificationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.notification.IpEventNotification _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.notification.IpEventNotificationHelper.narrow(_this_object(orb));
	}
	public IpEventNotificationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpEventNotificationOperations delegate)
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
	public int createNotification(org.csapi.fw.TpFwEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(eventCriteria);
	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.destroyNotification(assignmentID);
	}

}
