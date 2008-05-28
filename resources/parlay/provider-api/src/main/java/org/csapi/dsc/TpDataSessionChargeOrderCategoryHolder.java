package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionChargeOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargeOrderCategoryHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionChargeOrderCategory value;

	public TpDataSessionChargeOrderCategoryHolder ()
	{
	}
	public TpDataSessionChargeOrderCategoryHolder (final TpDataSessionChargeOrderCategory initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionChargeOrderCategoryHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionChargeOrderCategoryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionChargeOrderCategoryHelper.write (out,value);
	}
}
