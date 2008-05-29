package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMPresenceDataList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPresenceDataListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMPresenceData[] value;

	public TpPAMPresenceDataListHolder ()
	{
	}
	public TpPAMPresenceDataListHolder (final org.csapi.pam.TpPAMPresenceData[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMPresenceDataListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMPresenceDataListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMPresenceDataListHelper.write (out,value);
	}
}
