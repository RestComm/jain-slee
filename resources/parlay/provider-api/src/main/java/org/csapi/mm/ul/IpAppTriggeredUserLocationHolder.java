package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpAppTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppTriggeredUserLocationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppTriggeredUserLocation value;
	public IpAppTriggeredUserLocationHolder()
	{
	}
	public IpAppTriggeredUserLocationHolder (final IpAppTriggeredUserLocation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppTriggeredUserLocationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppTriggeredUserLocationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppTriggeredUserLocationHelper.write (_out,value);
	}
}
