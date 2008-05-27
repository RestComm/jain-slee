package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpMailboxIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpMailboxIdentifier value;

	public TpMailboxIdentifierHolder ()
	{
	}
	public TpMailboxIdentifierHolder(final org.csapi.gms.TpMailboxIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.TpMailboxIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.TpMailboxIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.TpMailboxIdentifierHelper.write(_out, value);
	}
}
