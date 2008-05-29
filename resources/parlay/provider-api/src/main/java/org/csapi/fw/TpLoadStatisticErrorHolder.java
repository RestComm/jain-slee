package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticError"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadStatisticError value;

	public TpLoadStatisticErrorHolder ()
	{
	}
	public TpLoadStatisticErrorHolder (final TpLoadStatisticError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticErrorHelper.write (out,value);
	}
}
