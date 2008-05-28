package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_CURRENCY"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_CURRENCYHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_CURRENCY value;

	public P_INVALID_CURRENCYHolder ()
	{
	}
	public P_INVALID_CURRENCYHolder(final org.csapi.P_INVALID_CURRENCY initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_CURRENCYHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_CURRENCYHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_CURRENCYHelper.write(_out, value);
	}
}
