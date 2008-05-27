package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSvcUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpSvcUnavailReasonHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSvcUnavailReason value;

	public TpSvcUnavailReasonHolder ()
	{
	}
	public TpSvcUnavailReasonHolder (final TpSvcUnavailReason initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSvcUnavailReasonHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSvcUnavailReasonHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSvcUnavailReasonHelper.write (out,value);
	}
}
