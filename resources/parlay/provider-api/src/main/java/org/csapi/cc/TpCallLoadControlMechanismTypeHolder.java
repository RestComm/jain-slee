package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallLoadControlMechanismType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLoadControlMechanismTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallLoadControlMechanismType value;

	public TpCallLoadControlMechanismTypeHolder ()
	{
	}
	public TpCallLoadControlMechanismTypeHolder (final TpCallLoadControlMechanismType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallLoadControlMechanismTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallLoadControlMechanismTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallLoadControlMechanismTypeHelper.write (out,value);
	}
}
