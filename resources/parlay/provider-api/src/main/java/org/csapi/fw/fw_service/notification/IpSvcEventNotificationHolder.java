package org.csapi.fw.fw_service.notification;

/**
 *	Generated from IDL interface "IpSvcEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSvcEventNotificationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSvcEventNotification value;
	public IpSvcEventNotificationHolder()
	{
	}
	public IpSvcEventNotificationHolder (final IpSvcEventNotification initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSvcEventNotificationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSvcEventNotificationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSvcEventNotificationHelper.write (_out,value);
	}
}
