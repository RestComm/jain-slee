package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_USER"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_USERHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.P_INVALID_USER value;

	public P_INVALID_USERHolder ()
	{
	}
	public P_INVALID_USERHolder(final org.csapi.cs.P_INVALID_USER initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.P_INVALID_USERHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.P_INVALID_USERHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.P_INVALID_USERHelper.write(_out, value);
	}
}
