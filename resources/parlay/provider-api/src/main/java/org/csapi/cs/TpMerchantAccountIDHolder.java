package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpMerchantAccountID"
 *	@author JacORB IDL compiler 
 */

public final class TpMerchantAccountIDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpMerchantAccountID value;

	public TpMerchantAccountIDHolder ()
	{
	}
	public TpMerchantAccountIDHolder(final org.csapi.cs.TpMerchantAccountID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpMerchantAccountIDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpMerchantAccountIDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpMerchantAccountIDHelper.write(_out, value);
	}
}
