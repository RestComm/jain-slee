package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpAppPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppPAMEventHandlerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppPAMEventHandler value;
	public IpAppPAMEventHandlerHolder()
	{
	}
	public IpAppPAMEventHandlerHolder (final IpAppPAMEventHandler initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppPAMEventHandlerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppPAMEventHandlerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppPAMEventHandlerHelper.write (_out,value);
	}
}
