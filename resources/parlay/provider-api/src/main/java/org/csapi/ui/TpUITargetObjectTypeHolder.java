package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUITargetObjectType"
 *	@author JacORB IDL compiler 
 */

public final class TpUITargetObjectTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUITargetObjectType value;

	public TpUITargetObjectTypeHolder ()
	{
	}
	public TpUITargetObjectTypeHolder (final TpUITargetObjectType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUITargetObjectTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUITargetObjectTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUITargetObjectTypeHelper.write (out,value);
	}
}
