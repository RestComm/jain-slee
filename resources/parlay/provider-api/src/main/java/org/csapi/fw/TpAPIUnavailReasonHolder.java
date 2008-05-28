package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpAPIUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpAPIUnavailReasonHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAPIUnavailReason value;

	public TpAPIUnavailReasonHolder ()
	{
	}
	public TpAPIUnavailReasonHolder (final TpAPIUnavailReason initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAPIUnavailReasonHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAPIUnavailReasonHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAPIUnavailReasonHelper.write (out,value);
	}
}
