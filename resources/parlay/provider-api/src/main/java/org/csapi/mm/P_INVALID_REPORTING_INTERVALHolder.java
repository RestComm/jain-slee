package org.csapi.mm;

/**
 *	Generated from IDL definition of exception "P_INVALID_REPORTING_INTERVAL"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_REPORTING_INTERVALHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.P_INVALID_REPORTING_INTERVAL value;

	public P_INVALID_REPORTING_INTERVALHolder ()
	{
	}
	public P_INVALID_REPORTING_INTERVALHolder(final org.csapi.mm.P_INVALID_REPORTING_INTERVAL initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.write(_out, value);
	}
}
