package org.csapi.fw.fw_service.notification;

/**
 *	Generated from IDL interface "IpFwEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwEventNotificationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwEventNotification value;
	public IpFwEventNotificationHolder()
	{
	}
	public IpFwEventNotificationHolder (final IpFwEventNotification initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwEventNotificationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwEventNotificationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwEventNotificationHelper.write (_out,value);
	}
}
