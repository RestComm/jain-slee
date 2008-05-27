package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressPlan"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressPlanHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAddressPlan value;

	public TpAddressPlanHolder ()
	{
	}
	public TpAddressPlanHolder (final TpAddressPlan initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAddressPlanHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAddressPlanHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAddressPlanHelper.write (out,value);
	}
}
