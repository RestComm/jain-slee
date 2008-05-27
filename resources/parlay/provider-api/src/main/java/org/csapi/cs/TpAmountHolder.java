package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpAmount"
 *	@author JacORB IDL compiler 
 */

public final class TpAmountHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpAmount value;

	public TpAmountHolder ()
	{
	}
	public TpAmountHolder(final org.csapi.cs.TpAmount initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpAmountHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpAmountHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpAmountHelper.write(_out, value);
	}
}
