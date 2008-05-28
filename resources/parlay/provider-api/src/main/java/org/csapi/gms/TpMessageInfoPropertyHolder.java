package org.csapi.gms;
/**
 *	Generated from IDL definition of union "TpMessageInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessageInfoProperty value;

	public TpMessageInfoPropertyHolder ()
	{
	}
	public TpMessageInfoPropertyHolder (final TpMessageInfoProperty initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessageInfoPropertyHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessageInfoPropertyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessageInfoPropertyHelper.write (out, value);
	}
}
