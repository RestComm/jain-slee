package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_INTERFACE_NAME"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_INTERFACE_NAMEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_INTERFACE_NAME value;

	public P_INVALID_INTERFACE_NAMEHolder ()
	{
	}
	public P_INVALID_INTERFACE_NAMEHolder(final org.csapi.P_INVALID_INTERFACE_NAME initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_INTERFACE_NAMEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_INTERFACE_NAMEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_INTERFACE_NAMEHelper.write(_out, value);
	}
}
