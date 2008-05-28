package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_VERSION"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_VERSIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_VERSION value;

	public P_INVALID_VERSIONHolder ()
	{
	}
	public P_INVALID_VERSIONHolder(final org.csapi.P_INVALID_VERSION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_VERSIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_VERSIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_VERSIONHelper.write(_out, value);
	}
}
