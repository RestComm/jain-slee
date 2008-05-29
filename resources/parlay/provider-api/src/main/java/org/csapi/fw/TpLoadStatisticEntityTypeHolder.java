package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticEntityType"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticEntityTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadStatisticEntityType value;

	public TpLoadStatisticEntityTypeHolder ()
	{
	}
	public TpLoadStatisticEntityTypeHolder (final TpLoadStatisticEntityType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticEntityTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticEntityTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticEntityTypeHelper.write (out,value);
	}
}
