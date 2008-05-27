package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpMailboxFolderIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxFolderIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpMailboxFolderIdentifier value;

	public TpMailboxFolderIdentifierHolder ()
	{
	}
	public TpMailboxFolderIdentifierHolder(final org.csapi.gms.TpMailboxFolderIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.TpMailboxFolderIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.TpMailboxFolderIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.TpMailboxFolderIdentifierHelper.write(_out, value);
	}
}
