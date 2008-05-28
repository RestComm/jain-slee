package org.csapi;

/**
 *	Generated from IDL definition of exception "TpCommonExceptions"
 *	@author JacORB IDL compiler 
 */

public final class TpCommonExceptionsHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpCommonExceptions value;

	public TpCommonExceptionsHolder ()
	{
	}
	public TpCommonExceptionsHolder(final org.csapi.TpCommonExceptions initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpCommonExceptionsHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpCommonExceptionsHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpCommonExceptionsHelper.write(_out, value);
	}
}
