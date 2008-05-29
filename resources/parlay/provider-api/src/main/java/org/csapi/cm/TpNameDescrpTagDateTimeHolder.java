package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagDateTime"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagDateTimeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagDateTime value;

	public TpNameDescrpTagDateTimeHolder ()
	{
	}
	public TpNameDescrpTagDateTimeHolder(final org.csapi.cm.TpNameDescrpTagDateTime initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagDateTimeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagDateTimeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagDateTimeHelper.write(_out, value);
	}
}
