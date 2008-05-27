package org.csapi;
/**
 *	Generated from IDL definition of enum "TpCallAoCOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAoCOrderCategoryHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAoCOrderCategory value;

	public TpCallAoCOrderCategoryHolder ()
	{
	}
	public TpCallAoCOrderCategoryHolder (final TpCallAoCOrderCategory initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAoCOrderCategoryHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAoCOrderCategoryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAoCOrderCategoryHelper.write (out,value);
	}
}
