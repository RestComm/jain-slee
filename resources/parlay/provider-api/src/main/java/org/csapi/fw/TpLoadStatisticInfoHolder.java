package org.csapi.fw;
/**
 *	Generated from IDL definition of union "TpLoadStatisticInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadStatisticInfo value;

	public TpLoadStatisticInfoHolder ()
	{
	}
	public TpLoadStatisticInfoHolder (final TpLoadStatisticInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticInfoHelper.write (out, value);
	}
}
