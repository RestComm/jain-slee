package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationTriggerCamel"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerCamelHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpLocationTriggerCamel value;

	public TpLocationTriggerCamelHolder ()
	{
	}
	public TpLocationTriggerCamelHolder(final org.csapi.mm.TpLocationTriggerCamel initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpLocationTriggerCamelHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpLocationTriggerCamelHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpLocationTriggerCamelHelper.write(_out, value);
	}
}
