package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagString"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagStringHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagString value;

	public TpNameDescrpTagStringHolder ()
	{
	}
	public TpNameDescrpTagStringHolder(final org.csapi.cm.TpNameDescrpTagString initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagStringHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagStringHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagStringHelper.write(_out, value);
	}
}
