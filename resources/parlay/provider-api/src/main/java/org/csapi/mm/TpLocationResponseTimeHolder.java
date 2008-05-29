package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationResponseTime"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationResponseTimeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpLocationResponseTime value;

	public TpLocationResponseTimeHolder ()
	{
	}
	public TpLocationResponseTimeHolder(final org.csapi.mm.TpLocationResponseTime initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpLocationResponseTimeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpLocationResponseTimeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpLocationResponseTimeHelper.write(_out, value);
	}
}
