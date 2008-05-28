package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpUIIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIIdentifier value;

	public TpUIIdentifierHolder ()
	{
	}
	public TpUIIdentifierHolder(final org.csapi.ui.TpUIIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUIIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUIIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUIIdentifierHelper.write(_out, value);
	}
}
