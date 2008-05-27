package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpCallLegHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpCallLeg value;
	public IpCallLegHolder()
	{
	}
	public IpCallLegHolder (final IpCallLeg initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpCallLegHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpCallLegHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpCallLegHelper.write (_out,value);
	}
}
