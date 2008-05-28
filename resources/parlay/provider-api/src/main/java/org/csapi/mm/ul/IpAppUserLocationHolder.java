package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpAppUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUserLocationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppUserLocation value;
	public IpAppUserLocationHolder()
	{
	}
	public IpAppUserLocationHolder (final IpAppUserLocation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppUserLocationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppUserLocationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppUserLocationHelper.write (_out,value);
	}
}
