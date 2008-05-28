package org.csapi.ui;

/**
 *	Generated from IDL definition of alias "TpUIVariableInfoSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariableInfoSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIVariableInfo[] value;

	public TpUIVariableInfoSetHolder ()
	{
	}
	public TpUIVariableInfoSetHolder (final org.csapi.ui.TpUIVariableInfo[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIVariableInfoSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIVariableInfoSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIVariableInfoSetHelper.write (out,value);
	}
}
