package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMAvailability"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMAvailabilityHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMAvailability value;
	public IpPAMAvailabilityHolder()
	{
	}
	public IpPAMAvailabilityHolder (final IpPAMAvailability initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMAvailabilityHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMAvailabilityHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMAvailabilityHelper.write (_out,value);
	}
}
