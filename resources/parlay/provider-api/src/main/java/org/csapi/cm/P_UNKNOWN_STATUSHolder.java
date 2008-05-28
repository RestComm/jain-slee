package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_STATUS"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_STATUSHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_STATUS value;

	public P_UNKNOWN_STATUSHolder ()
	{
	}
	public P_UNKNOWN_STATUSHolder(final org.csapi.cm.P_UNKNOWN_STATUS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_STATUSHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_STATUSHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_STATUSHelper.write(_out, value);
	}
}
