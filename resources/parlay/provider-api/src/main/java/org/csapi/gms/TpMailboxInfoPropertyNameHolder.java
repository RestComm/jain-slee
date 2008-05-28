package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMailboxInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertyNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMailboxInfoPropertyName value;

	public TpMailboxInfoPropertyNameHolder ()
	{
	}
	public TpMailboxInfoPropertyNameHolder (final TpMailboxInfoPropertyName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMailboxInfoPropertyNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMailboxInfoPropertyNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMailboxInfoPropertyNameHelper.write (out,value);
	}
}
