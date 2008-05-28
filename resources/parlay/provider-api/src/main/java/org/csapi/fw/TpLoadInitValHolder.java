package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadInitVal"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadInitValHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadInitVal value;

	public TpLoadInitValHolder ()
	{
	}
	public TpLoadInitValHolder(final org.csapi.fw.TpLoadInitVal initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpLoadInitValHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpLoadInitValHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpLoadInitValHelper.write(_out, value);
	}
}
