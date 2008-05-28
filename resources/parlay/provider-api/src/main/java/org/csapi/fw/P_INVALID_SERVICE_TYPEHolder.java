package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_SERVICE_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SERVICE_TYPEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_SERVICE_TYPE value;

	public P_INVALID_SERVICE_TYPEHolder ()
	{
	}
	public P_INVALID_SERVICE_TYPEHolder(final org.csapi.fw.P_INVALID_SERVICE_TYPE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_SERVICE_TYPEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_SERVICE_TYPEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_SERVICE_TYPEHelper.write(_out, value);
	}
}
