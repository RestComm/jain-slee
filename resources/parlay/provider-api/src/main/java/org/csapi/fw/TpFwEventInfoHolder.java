package org.csapi.fw;
/**
 *	Generated from IDL definition of union "TpFwEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFwEventInfo value;

	public TpFwEventInfoHolder ()
	{
	}
	public TpFwEventInfoHolder (final TpFwEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFwEventInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFwEventInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFwEventInfoHelper.write (out, value);
	}
}
