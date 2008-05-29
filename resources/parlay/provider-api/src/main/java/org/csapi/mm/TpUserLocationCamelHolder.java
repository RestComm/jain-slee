package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationCamel"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationCamelHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserLocationCamel value;

	public TpUserLocationCamelHolder ()
	{
	}
	public TpUserLocationCamelHolder(final org.csapi.mm.TpUserLocationCamel initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpUserLocationCamelHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpUserLocationCamelHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpUserLocationCamelHelper.write(_out, value);
	}
}
