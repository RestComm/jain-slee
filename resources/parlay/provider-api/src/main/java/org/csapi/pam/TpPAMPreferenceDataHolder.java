package org.csapi.pam;
/**
 *	Generated from IDL definition of union "TpPAMPreferenceData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPreferenceDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMPreferenceData value;

	public TpPAMPreferenceDataHolder ()
	{
	}
	public TpPAMPreferenceDataHolder (final TpPAMPreferenceData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMPreferenceDataHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMPreferenceDataHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMPreferenceDataHelper.write (out, value);
	}
}
