package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIFault"
 *	@author JacORB IDL compiler 
 */

public final class TpUIFaultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIFault value;

	public TpUIFaultHolder ()
	{
	}
	public TpUIFaultHolder (final TpUIFault initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIFaultHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIFaultHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIFaultHelper.write (out,value);
	}
}
