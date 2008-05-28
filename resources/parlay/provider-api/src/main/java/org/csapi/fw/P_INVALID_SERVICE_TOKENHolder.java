package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_SERVICE_TOKEN"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SERVICE_TOKENHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_SERVICE_TOKEN value;

	public P_INVALID_SERVICE_TOKENHolder ()
	{
	}
	public P_INVALID_SERVICE_TOKENHolder(final org.csapi.fw.P_INVALID_SERVICE_TOKEN initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.write(_out, value);
	}
}
