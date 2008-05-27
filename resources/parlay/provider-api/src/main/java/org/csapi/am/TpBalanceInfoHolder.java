package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpBalanceInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpBalanceInfo value;

	public TpBalanceInfoHolder ()
	{
	}
	public TpBalanceInfoHolder(final org.csapi.am.TpBalanceInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.TpBalanceInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.TpBalanceInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.TpBalanceInfoHelper.write(_out, value);
	}
}
