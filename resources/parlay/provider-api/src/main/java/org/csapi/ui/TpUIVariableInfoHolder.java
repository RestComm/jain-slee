package org.csapi.ui;
/**
 *	Generated from IDL definition of union "TpUIVariableInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariableInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIVariableInfo value;

	public TpUIVariableInfoHolder ()
	{
	}
	public TpUIVariableInfoHolder (final TpUIVariableInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIVariableInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIVariableInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIVariableInfoHelper.write (out, value);
	}
}
