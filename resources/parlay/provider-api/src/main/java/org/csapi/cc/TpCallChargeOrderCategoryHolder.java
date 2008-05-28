package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallChargeOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallChargeOrderCategoryHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallChargeOrderCategory value;

	public TpCallChargeOrderCategoryHolder ()
	{
	}
	public TpCallChargeOrderCategoryHolder (final TpCallChargeOrderCategory initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallChargeOrderCategoryHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallChargeOrderCategoryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallChargeOrderCategoryHelper.write (out,value);
	}
}
