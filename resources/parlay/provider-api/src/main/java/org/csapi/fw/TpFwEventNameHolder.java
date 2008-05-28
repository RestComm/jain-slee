package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFwEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFwEventName value;

	public TpFwEventNameHolder ()
	{
	}
	public TpFwEventNameHolder (final TpFwEventName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFwEventNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFwEventNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFwEventNameHelper.write (out,value);
	}
}
