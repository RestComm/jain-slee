package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpTriggeredUserLocationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpTriggeredUserLocation value;
	public IpTriggeredUserLocationHolder()
	{
	}
	public IpTriggeredUserLocationHolder (final IpTriggeredUserLocation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpTriggeredUserLocationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpTriggeredUserLocationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpTriggeredUserLocationHelper.write (_out,value);
	}
}
