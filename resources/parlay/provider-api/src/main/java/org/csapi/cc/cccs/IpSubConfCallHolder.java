package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpSubConfCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpSubConfCall value;
	public IpSubConfCallHolder()
	{
	}
	public IpSubConfCallHolder (final IpSubConfCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpSubConfCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpSubConfCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpSubConfCallHelper.write (_out,value);
	}
}
