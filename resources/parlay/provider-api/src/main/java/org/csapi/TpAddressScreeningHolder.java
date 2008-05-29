package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressScreening"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressScreeningHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAddressScreening value;

	public TpAddressScreeningHolder ()
	{
	}
	public TpAddressScreeningHolder (final TpAddressScreening initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAddressScreeningHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAddressScreeningHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAddressScreeningHelper.write (out,value);
	}
}
