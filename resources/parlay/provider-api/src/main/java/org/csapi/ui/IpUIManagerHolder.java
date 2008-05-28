package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUIManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUIManager value;
	public IpUIManagerHolder()
	{
	}
	public IpUIManagerHolder (final IpUIManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUIManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUIManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUIManagerHelper.write (_out,value);
	}
}
