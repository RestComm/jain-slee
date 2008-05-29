package org.csapi.fw.fw_application.notification;

/**
 *	Generated from IDL interface "IpEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpEventNotificationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpEventNotification value;
	public IpEventNotificationHolder()
	{
	}
	public IpEventNotificationHolder (final IpEventNotification initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpEventNotificationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpEventNotificationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpEventNotificationHelper.write (_out,value);
	}
}
