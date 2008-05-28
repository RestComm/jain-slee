package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAvailabilityProfile"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAvailabilityProfileHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAvailabilityProfile value;

	public TpPAMAvailabilityProfileHolder ()
	{
	}
	public TpPAMAvailabilityProfileHolder(final org.csapi.pam.TpPAMAvailabilityProfile initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAvailabilityProfileHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAvailabilityProfileHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAvailabilityProfileHelper.write(_out, value);
	}
}
