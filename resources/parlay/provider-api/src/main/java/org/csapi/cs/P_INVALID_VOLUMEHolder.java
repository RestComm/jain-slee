package org.csapi.cs;

/**
 *	Generated from IDL definition of exception "P_INVALID_VOLUME"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_VOLUMEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.P_INVALID_VOLUME value;

	public P_INVALID_VOLUMEHolder ()
	{
	}
	public P_INVALID_VOLUMEHolder(final org.csapi.cs.P_INVALID_VOLUME initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.P_INVALID_VOLUMEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.P_INVALID_VOLUMEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.P_INVALID_VOLUMEHelper.write(_out, value);
	}
}
