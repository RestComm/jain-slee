package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagDir"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagDirHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagDir value;

	public TpNameDescrpTagDirHolder ()
	{
	}
	public TpNameDescrpTagDirHolder(final org.csapi.cm.TpNameDescrpTagDir initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagDirHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagDirHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagDirHelper.write(_out, value);
	}
}
