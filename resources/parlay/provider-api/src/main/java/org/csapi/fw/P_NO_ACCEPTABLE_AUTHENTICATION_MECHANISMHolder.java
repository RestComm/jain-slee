package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM"
 *	@author JacORB IDL compiler 
 */

public final class P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISMHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM value;

	public P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISMHolder ()
	{
	}
	public P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISMHolder(final org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISMHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISMHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISMHelper.write(_out, value);
	}
}
