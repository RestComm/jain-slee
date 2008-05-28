package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpBalanceQueryError"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceQueryErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpBalanceQueryError value;

	public TpBalanceQueryErrorHolder ()
	{
	}
	public TpBalanceQueryErrorHolder (final TpBalanceQueryError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpBalanceQueryErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpBalanceQueryErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpBalanceQueryErrorHelper.write (out,value);
	}
}
