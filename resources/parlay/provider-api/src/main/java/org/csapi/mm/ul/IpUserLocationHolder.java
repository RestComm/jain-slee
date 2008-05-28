package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUserLocationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUserLocation value;
	public IpUserLocationHolder()
	{
	}
	public IpUserLocationHolder (final IpUserLocation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUserLocationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUserLocationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUserLocationHelper.write (_out,value);
	}
}
