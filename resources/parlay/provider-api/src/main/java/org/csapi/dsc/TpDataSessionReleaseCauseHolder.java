package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReleaseCauseHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionReleaseCause value;

	public TpDataSessionReleaseCauseHolder ()
	{
	}
	public TpDataSessionReleaseCauseHolder(final org.csapi.dsc.TpDataSessionReleaseCause initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionReleaseCauseHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionReleaseCauseHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionReleaseCauseHelper.write(_out, value);
	}
}
