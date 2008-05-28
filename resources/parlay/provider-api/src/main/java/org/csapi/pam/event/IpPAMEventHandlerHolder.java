package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMEventHandlerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMEventHandler value;
	public IpPAMEventHandlerHolder()
	{
	}
	public IpPAMEventHandlerHolder (final IpPAMEventHandler initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMEventHandlerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMEventHandlerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMEventHandlerHelper.write (_out,value);
	}
}
