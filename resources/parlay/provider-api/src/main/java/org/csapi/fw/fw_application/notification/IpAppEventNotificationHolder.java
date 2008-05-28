package org.csapi.fw.fw_application.notification;

/**
 *	Generated from IDL interface "IpAppEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppEventNotificationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppEventNotification value;
	public IpAppEventNotificationHolder()
	{
	}
	public IpAppEventNotificationHolder (final IpAppEventNotification initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppEventNotificationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppEventNotificationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppEventNotificationHelper.write (_out,value);
	}
}
