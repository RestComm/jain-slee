package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUICallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppUICall value;
	public IpAppUICallHolder()
	{
	}
	public IpAppUICallHolder (final IpAppUICall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppUICallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppUICallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppUICallHelper.write (_out,value);
	}
}
