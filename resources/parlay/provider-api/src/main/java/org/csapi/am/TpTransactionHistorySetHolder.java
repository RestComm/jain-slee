package org.csapi.am;

/**
 *	Generated from IDL definition of alias "TpTransactionHistorySet"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistorySetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpTransactionHistory[] value;

	public TpTransactionHistorySetHolder ()
	{
	}
	public TpTransactionHistorySetHolder (final org.csapi.am.TpTransactionHistory[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTransactionHistorySetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTransactionHistorySetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTransactionHistorySetHelper.write (out,value);
	}
}
