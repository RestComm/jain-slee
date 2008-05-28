package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpConfCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpConfCall value;
	public IpConfCallHolder()
	{
	}
	public IpConfCallHolder (final IpConfCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpConfCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpConfCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpConfCallHelper.write (_out,value);
	}
}
