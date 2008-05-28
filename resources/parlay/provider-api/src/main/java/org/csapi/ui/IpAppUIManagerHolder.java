package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUIManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppUIManager value;
	public IpAppUIManagerHolder()
	{
	}
	public IpAppUIManagerHolder (final IpAppUIManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppUIManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppUIManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppUIManagerHelper.write (_out,value);
	}
}
