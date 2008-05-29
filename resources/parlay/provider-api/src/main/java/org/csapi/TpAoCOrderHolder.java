package org.csapi;
/**
 *	Generated from IDL definition of union "TpAoCOrder"
 *	@author JacORB IDL compiler 
 */

public final class TpAoCOrderHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAoCOrder value;

	public TpAoCOrderHolder ()
	{
	}
	public TpAoCOrderHolder (final TpAoCOrder initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAoCOrderHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAoCOrderHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAoCOrderHelper.write (out, value);
	}
}
