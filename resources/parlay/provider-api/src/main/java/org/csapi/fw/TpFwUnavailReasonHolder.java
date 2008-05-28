package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFwUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpFwUnavailReasonHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFwUnavailReason value;

	public TpFwUnavailReasonHolder ()
	{
	}
	public TpFwUnavailReasonHolder (final TpFwUnavailReason initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFwUnavailReasonHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFwUnavailReasonHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFwUnavailReasonHelper.write (out,value);
	}
}
