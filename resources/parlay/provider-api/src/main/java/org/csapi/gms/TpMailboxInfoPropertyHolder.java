package org.csapi.gms;
/**
 *	Generated from IDL definition of union "TpMailboxInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMailboxInfoProperty value;

	public TpMailboxInfoPropertyHolder ()
	{
	}
	public TpMailboxInfoPropertyHolder (final TpMailboxInfoProperty initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMailboxInfoPropertyHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMailboxInfoPropertyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMailboxInfoPropertyHelper.write (out, value);
	}
}
