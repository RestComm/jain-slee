package org.csapi.mm.ule;

/**
 *	Generated from IDL interface "IpAppUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUserLocationEmergencyHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppUserLocationEmergency value;
	public IpAppUserLocationEmergencyHolder()
	{
	}
	public IpAppUserLocationEmergencyHolder (final IpAppUserLocationEmergency initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppUserLocationEmergencyHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppUserLocationEmergencyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppUserLocationEmergencyHelper.write (_out,value);
	}
}
