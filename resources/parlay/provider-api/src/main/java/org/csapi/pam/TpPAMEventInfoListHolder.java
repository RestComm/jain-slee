package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMEventInfoList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventInfoListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMEventInfo[] value;

	public TpPAMEventInfoListHolder ()
	{
	}
	public TpPAMEventInfoListHolder (final org.csapi.pam.TpPAMEventInfo[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMEventInfoListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMEventInfoListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMEventInfoListHelper.write (out,value);
	}
}
