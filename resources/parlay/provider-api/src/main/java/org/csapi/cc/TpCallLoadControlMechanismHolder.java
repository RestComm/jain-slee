package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpCallLoadControlMechanism"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLoadControlMechanismHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallLoadControlMechanism value;

	public TpCallLoadControlMechanismHolder ()
	{
	}
	public TpCallLoadControlMechanismHolder (final TpCallLoadControlMechanism initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallLoadControlMechanismHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallLoadControlMechanismHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallLoadControlMechanismHelper.write (out, value);
	}
}
