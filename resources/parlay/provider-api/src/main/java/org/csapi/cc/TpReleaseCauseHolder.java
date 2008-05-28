package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpReleaseCauseHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpReleaseCause value;

	public TpReleaseCauseHolder ()
	{
	}
	public TpReleaseCauseHolder (final TpReleaseCause initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpReleaseCauseHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpReleaseCauseHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpReleaseCauseHelper.write (out,value);
	}
}
