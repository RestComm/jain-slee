package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_REQUEST_NUMBER"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_REQUEST_NUMBERHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.P_INVALID_REQUEST_NUMBER value;

	public P_INVALID_REQUEST_NUMBERHolder ()
	{
	}
	public P_INVALID_REQUEST_NUMBERHolder(final org.csapi.cs.P_INVALID_REQUEST_NUMBER initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, value);
	}
}
