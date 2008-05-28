package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMErrorCause"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMErrorCauseHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMErrorCause value;

	public TpPAMErrorCauseHolder ()
	{
	}
	public TpPAMErrorCauseHolder (final TpPAMErrorCause initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMErrorCauseHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMErrorCauseHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMErrorCauseHelper.write (out,value);
	}
}
