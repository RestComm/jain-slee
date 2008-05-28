package org.csapi.fw;
/**
 *	Generated from IDL definition of union "TpLoadStatisticEntityID"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticEntityIDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadStatisticEntityID value;

	public TpLoadStatisticEntityIDHolder ()
	{
	}
	public TpLoadStatisticEntityIDHolder (final TpLoadStatisticEntityID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticEntityIDHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticEntityIDHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticEntityIDHelper.write (out, value);
	}
}
