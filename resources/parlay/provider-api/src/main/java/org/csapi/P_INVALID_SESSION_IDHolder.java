package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_SESSION_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SESSION_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_SESSION_ID value;

	public P_INVALID_SESSION_IDHolder ()
	{
	}
	public P_INVALID_SESSION_IDHolder(final org.csapi.P_INVALID_SESSION_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_SESSION_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_SESSION_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_SESSION_IDHelper.write(_out, value);
	}
}
