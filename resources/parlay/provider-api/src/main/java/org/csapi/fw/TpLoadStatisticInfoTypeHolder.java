package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticInfoTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadStatisticInfoType value;

	public TpLoadStatisticInfoTypeHolder ()
	{
	}
	public TpLoadStatisticInfoTypeHolder (final TpLoadStatisticInfoType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticInfoTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticInfoTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticInfoTypeHelper.write (out,value);
	}
}
