package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpAppSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppSubConfCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppSubConfCall value;
	public IpAppSubConfCallHolder()
	{
	}
	public IpAppSubConfCallHolder (final IpAppSubConfCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppSubConfCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppSubConfCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppSubConfCallHelper.write (_out,value);
	}
}
