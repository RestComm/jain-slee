package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpAppAvailStatusReason"
 *	@author JacORB IDL compiler 
 */

public final class TpAppAvailStatusReasonHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppAvailStatusReason value;

	public TpAppAvailStatusReasonHolder ()
	{
	}
	public TpAppAvailStatusReasonHolder (final TpAppAvailStatusReason initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppAvailStatusReasonHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppAvailStatusReasonHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppAvailStatusReasonHelper.write (out,value);
	}
}
