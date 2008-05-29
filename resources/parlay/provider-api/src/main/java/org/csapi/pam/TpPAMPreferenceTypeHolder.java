package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMPreferenceType"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPreferenceTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMPreferenceType value;

	public TpPAMPreferenceTypeHolder ()
	{
	}
	public TpPAMPreferenceTypeHolder (final TpPAMPreferenceType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMPreferenceTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMPreferenceTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMPreferenceTypeHelper.write (out,value);
	}
}
