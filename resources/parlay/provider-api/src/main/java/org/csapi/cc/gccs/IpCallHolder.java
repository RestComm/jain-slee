package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpCall value;
	public IpCallHolder()
	{
	}
	public IpCallHolder (final IpCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpCallHelper.write (_out,value);
	}
}
