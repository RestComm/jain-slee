package org.csapi;

/**
 *	Generated from IDL definition of struct "TpTimeInterval"
 *	@author JacORB IDL compiler 
 */

public final class TpTimeIntervalHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpTimeInterval value;

	public TpTimeIntervalHolder ()
	{
	}
	public TpTimeIntervalHolder(final org.csapi.TpTimeInterval initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpTimeIntervalHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpTimeIntervalHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpTimeIntervalHelper.write(_out, value);
	}
}
