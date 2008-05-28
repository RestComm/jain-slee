package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagMonth"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagMonthHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagMonth value;

	public TpNameDescrpTagMonthHolder ()
	{
	}
	public TpNameDescrpTagMonthHolder(final org.csapi.cm.TpNameDescrpTagMonth initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagMonthHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagMonthHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagMonthHelper.write(_out, value);
	}
}
