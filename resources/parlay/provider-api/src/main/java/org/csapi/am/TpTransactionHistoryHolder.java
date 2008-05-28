package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpTransactionHistory"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistoryHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpTransactionHistory value;

	public TpTransactionHistoryHolder ()
	{
	}
	public TpTransactionHistoryHolder(final org.csapi.am.TpTransactionHistory initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.TpTransactionHistoryHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.TpTransactionHistoryHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.TpTransactionHistoryHelper.write(_out, value);
	}
}
