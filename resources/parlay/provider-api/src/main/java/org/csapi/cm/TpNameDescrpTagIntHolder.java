package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagInt"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagIntHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagInt value;

	public TpNameDescrpTagIntHolder ()
	{
	}
	public TpNameDescrpTagIntHolder(final org.csapi.cm.TpNameDescrpTagInt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagIntHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagIntHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagIntHelper.write(_out, value);
	}
}
