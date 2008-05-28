package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpAppCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppCall value;
	public IpAppCallHolder()
	{
	}
	public IpAppCallHolder (final IpAppCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppCallHelper.write (_out,value);
	}
}
