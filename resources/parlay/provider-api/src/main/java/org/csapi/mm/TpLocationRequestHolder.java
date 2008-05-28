package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpLocationRequest value;

	public TpLocationRequestHolder ()
	{
	}
	public TpLocationRequestHolder(final org.csapi.mm.TpLocationRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpLocationRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpLocationRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpLocationRequestHelper.write(_out, value);
	}
}
