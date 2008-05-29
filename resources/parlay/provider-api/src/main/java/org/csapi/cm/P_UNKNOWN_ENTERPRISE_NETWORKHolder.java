package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_ENTERPRISE_NETWORK"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_ENTERPRISE_NETWORKHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORK value;

	public P_UNKNOWN_ENTERPRISE_NETWORKHolder ()
	{
	}
	public P_UNKNOWN_ENTERPRISE_NETWORKHolder(final org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORK initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORKHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORKHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORKHelper.write(_out, value);
	}
}
