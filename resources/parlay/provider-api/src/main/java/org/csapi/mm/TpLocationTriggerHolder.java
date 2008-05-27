package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationTrigger"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpLocationTrigger value;

	public TpLocationTriggerHolder ()
	{
	}
	public TpLocationTriggerHolder(final org.csapi.mm.TpLocationTrigger initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpLocationTriggerHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpLocationTriggerHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpLocationTriggerHelper.write(_out, value);
	}
}
