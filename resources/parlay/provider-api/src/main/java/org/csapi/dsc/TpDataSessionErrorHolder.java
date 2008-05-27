package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionError"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionError value;

	public TpDataSessionErrorHolder ()
	{
	}
	public TpDataSessionErrorHolder(final org.csapi.dsc.TpDataSessionError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionErrorHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionErrorHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionErrorHelper.write(_out, value);
	}
}
