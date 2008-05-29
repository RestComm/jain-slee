package org.csapi.fw.fw_service.notification;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwEventNotificationPOATie
	extends IpFwEventNotificationPOA
{
	private IpFwEventNotificationOperations _delegate;

	private POA _poa;
	public IpFwEventNotificationPOATie(IpFwEventNotificationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwEventNotificationPOATie(IpFwEventNotificationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.notification.IpFwEventNotification _this()
	{
		return org.csapi.fw.fw_service.notification.IpFwEventNotificationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.notification.IpFwEventNotification _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.notification.IpFwEventNotificationHelper.narrow(_this_object(orb));
	}
	public IpFwEventNotificationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwEventNotificationOperations delegate)
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
	public int createNotification(org.csapi.fw.TpFwEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(eventCriteria);
	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.destroyNotification(assignmentID);
	}

}
