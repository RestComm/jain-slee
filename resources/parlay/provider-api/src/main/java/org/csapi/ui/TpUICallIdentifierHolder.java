package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUICallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpUICallIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUICallIdentifier value;

	public TpUICallIdentifierHolder ()
	{
	}
	public TpUICallIdentifierHolder(final org.csapi.ui.TpUICallIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUICallIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUICallIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUICallIdentifierHelper.write(_out, value);
	}
}
