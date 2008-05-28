package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageFormat"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageFormatHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessageFormat value;

	public TpMessageFormatHolder ()
	{
	}
	public TpMessageFormatHolder (final TpMessageFormat initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessageFormatHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessageFormatHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessageFormatHelper.write (out,value);
	}
}
