package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpCorrelationID"
 *	@author JacORB IDL compiler 
 */

public final class TpCorrelationIDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpCorrelationID value;

	public TpCorrelationIDHolder ()
	{
	}
	public TpCorrelationIDHolder(final org.csapi.cs.TpCorrelationID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpCorrelationIDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpCorrelationIDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpCorrelationIDHelper.write(_out, value);
	}
}
