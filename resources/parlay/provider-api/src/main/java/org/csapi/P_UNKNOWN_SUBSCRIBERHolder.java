package org.csapi;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SUBSCRIBER"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SUBSCRIBERHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_UNKNOWN_SUBSCRIBER value;

	public P_UNKNOWN_SUBSCRIBERHolder ()
	{
	}
	public P_UNKNOWN_SUBSCRIBERHolder(final org.csapi.P_UNKNOWN_SUBSCRIBER initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_UNKNOWN_SUBSCRIBERHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_UNKNOWN_SUBSCRIBERHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, value);
	}
}
