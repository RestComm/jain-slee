package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMCapabilityList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCapabilityListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpPAMCapabilityListHolder ()
	{
	}
	public TpPAMCapabilityListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMCapabilityListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMCapabilityListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMCapabilityListHelper.write (out,value);
	}
}
