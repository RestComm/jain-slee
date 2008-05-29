package org.csapi.dsc;
/**
 *	Generated from IDL definition of union "TpDataSessionChargeOrder"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargeOrderHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionChargeOrder value;

	public TpDataSessionChargeOrderHolder ()
	{
	}
	public TpDataSessionChargeOrderHolder (final TpDataSessionChargeOrder initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionChargeOrderHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionChargeOrderHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionChargeOrderHelper.write (out, value);
	}
}
