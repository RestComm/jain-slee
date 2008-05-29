package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSvcAvailStatusReason"
 *	@author JacORB IDL compiler 
 */

public final class TpSvcAvailStatusReasonHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSvcAvailStatusReason value;

	public TpSvcAvailStatusReasonHolder ()
	{
	}
	public TpSvcAvailStatusReasonHolder (final TpSvcAvailStatusReason initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSvcAvailStatusReasonHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSvcAvailStatusReasonHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSvcAvailStatusReasonHelper.write (out,value);
	}
}
