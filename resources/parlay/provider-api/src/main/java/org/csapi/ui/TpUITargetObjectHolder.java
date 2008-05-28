package org.csapi.ui;
/**
 *	Generated from IDL definition of union "TpUITargetObject"
 *	@author JacORB IDL compiler 
 */

public final class TpUITargetObjectHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUITargetObject value;

	public TpUITargetObjectHolder ()
	{
	}
	public TpUITargetObjectHolder (final TpUITargetObject initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUITargetObjectHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUITargetObjectHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUITargetObjectHelper.write (out, value);
	}
}
