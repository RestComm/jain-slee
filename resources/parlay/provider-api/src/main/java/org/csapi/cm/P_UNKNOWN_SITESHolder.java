package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SITES"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SITESHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_SITES value;

	public P_UNKNOWN_SITESHolder ()
	{
	}
	public P_UNKNOWN_SITESHolder(final org.csapi.cm.P_UNKNOWN_SITES initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_SITESHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_SITESHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_SITESHelper.write(_out, value);
	}
}
