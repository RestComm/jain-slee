package org.csapi.mm.ule;

/**
 *	Generated from IDL interface "IpUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUserLocationEmergencyHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUserLocationEmergency value;
	public IpUserLocationEmergencyHolder()
	{
	}
	public IpUserLocationEmergencyHolder (final IpUserLocationEmergency initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUserLocationEmergencyHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUserLocationEmergencyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUserLocationEmergencyHelper.write (_out,value);
	}
}
