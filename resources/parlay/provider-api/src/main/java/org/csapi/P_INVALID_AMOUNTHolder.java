package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_AMOUNT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_AMOUNTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_AMOUNT value;

	public P_INVALID_AMOUNTHolder ()
	{
	}
	public P_INVALID_AMOUNTHolder(final org.csapi.P_INVALID_AMOUNT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_AMOUNTHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_AMOUNTHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_AMOUNTHelper.write(_out, value);
	}
}
