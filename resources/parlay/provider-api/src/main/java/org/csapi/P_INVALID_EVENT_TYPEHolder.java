package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_EVENT_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_EVENT_TYPEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_EVENT_TYPE value;

	public P_INVALID_EVENT_TYPEHolder ()
	{
	}
	public P_INVALID_EVENT_TYPEHolder(final org.csapi.P_INVALID_EVENT_TYPE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_EVENT_TYPEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_EVENT_TYPEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_EVENT_TYPEHelper.write(_out, value);
	}
}
