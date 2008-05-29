package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppCallLegHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppCallLeg value;
	public IpAppCallLegHolder()
	{
	}
	public IpAppCallLegHolder (final IpAppCallLeg initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppCallLegHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppCallLegHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppCallLegHelper.write (_out,value);
	}
}
