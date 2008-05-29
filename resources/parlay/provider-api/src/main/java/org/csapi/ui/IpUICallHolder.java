package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUICallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUICall value;
	public IpUICallHolder()
	{
	}
	public IpUICallHolder (final IpUICall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUICallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUICallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUICallHelper.write (_out,value);
	}
}
