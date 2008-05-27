package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpSessionEndedCause"
 *	@author JacORB IDL compiler 
 */

public final class TpSessionEndedCauseHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSessionEndedCause value;

	public TpSessionEndedCauseHolder ()
	{
	}
	public TpSessionEndedCauseHolder (final TpSessionEndedCause initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSessionEndedCauseHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSessionEndedCauseHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSessionEndedCauseHelper.write (out,value);
	}
}
