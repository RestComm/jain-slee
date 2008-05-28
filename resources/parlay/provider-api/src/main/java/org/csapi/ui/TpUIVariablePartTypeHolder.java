package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIVariablePartType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariablePartTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIVariablePartType value;

	public TpUIVariablePartTypeHolder ()
	{
	}
	public TpUIVariablePartTypeHolder (final TpUIVariablePartType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIVariablePartTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIVariablePartTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIVariablePartTypeHelper.write (out,value);
	}
}
