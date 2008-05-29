package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpTransactionHistoryStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistoryStatusHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpTransactionHistoryStatus value;

	public TpTransactionHistoryStatusHolder ()
	{
	}
	public TpTransactionHistoryStatusHolder (final TpTransactionHistoryStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTransactionHistoryStatusHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTransactionHistoryStatusHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTransactionHistoryStatusHelper.write (out,value);
	}
}
