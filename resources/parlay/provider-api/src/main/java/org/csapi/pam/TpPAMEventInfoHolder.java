package org.csapi.pam;
/**
 *	Generated from IDL definition of union "TpPAMEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMEventInfo value;

	public TpPAMEventInfoHolder ()
	{
	}
	public TpPAMEventInfoHolder (final TpPAMEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMEventInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMEventInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMEventInfoHelper.write (out, value);
	}
}
