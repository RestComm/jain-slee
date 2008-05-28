package org.csapi.termcap;

/**
 *	Generated from IDL definition of exception "P_INVALID_TERMINAL_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_TERMINAL_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.termcap.P_INVALID_TERMINAL_ID value;

	public P_INVALID_TERMINAL_IDHolder ()
	{
	}
	public P_INVALID_TERMINAL_IDHolder(final org.csapi.termcap.P_INVALID_TERMINAL_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.write(_out, value);
	}
}
