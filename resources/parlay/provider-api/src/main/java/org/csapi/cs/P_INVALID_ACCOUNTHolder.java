package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_ACCOUNT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ACCOUNTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.P_INVALID_ACCOUNT value;

	public P_INVALID_ACCOUNTHolder ()
	{
	}
	public P_INVALID_ACCOUNTHolder(final org.csapi.cs.P_INVALID_ACCOUNT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.P_INVALID_ACCOUNTHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.P_INVALID_ACCOUNTHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.P_INVALID_ACCOUNTHelper.write(_out, value);
	}
}
