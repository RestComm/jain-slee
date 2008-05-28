package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_NETWORK_STATE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_NETWORK_STATEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_NETWORK_STATE value;

	public P_INVALID_NETWORK_STATEHolder ()
	{
	}
	public P_INVALID_NETWORK_STATEHolder(final org.csapi.P_INVALID_NETWORK_STATE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_NETWORK_STATEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_NETWORK_STATEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_NETWORK_STATEHelper.write(_out, value);
	}
}
