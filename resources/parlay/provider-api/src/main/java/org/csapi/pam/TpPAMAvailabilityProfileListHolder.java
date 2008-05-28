package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMAvailabilityProfileList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAvailabilityProfileListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAvailabilityProfile[] value;

	public TpPAMAvailabilityProfileListHolder ()
	{
	}
	public TpPAMAvailabilityProfileListHolder (final org.csapi.pam.TpPAMAvailabilityProfile[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMAvailabilityProfileListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMAvailabilityProfileListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMAvailabilityProfileListHelper.write (out,value);
	}
}
