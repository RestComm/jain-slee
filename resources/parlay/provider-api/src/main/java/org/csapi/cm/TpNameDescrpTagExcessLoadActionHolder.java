package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpNameDescrpTagExcessLoadAction"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagExcessLoadActionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpNameDescrpTagExcessLoadAction value;

	public TpNameDescrpTagExcessLoadActionHolder ()
	{
	}
	public TpNameDescrpTagExcessLoadActionHolder(final org.csapi.cm.TpNameDescrpTagExcessLoadAction initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpNameDescrpTagExcessLoadActionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpNameDescrpTagExcessLoadActionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpNameDescrpTagExcessLoadActionHelper.write(_out, value);
	}
}
