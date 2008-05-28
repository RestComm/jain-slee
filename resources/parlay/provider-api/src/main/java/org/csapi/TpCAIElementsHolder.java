package org.csapi;

/**
 *	Generated from IDL definition of struct "TpCAIElements"
 *	@author JacORB IDL compiler 
 */

public final class TpCAIElementsHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpCAIElements value;

	public TpCAIElementsHolder ()
	{
	}
	public TpCAIElementsHolder(final org.csapi.TpCAIElements initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpCAIElementsHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpCAIElementsHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpCAIElementsHelper.write(_out, value);
	}
}
