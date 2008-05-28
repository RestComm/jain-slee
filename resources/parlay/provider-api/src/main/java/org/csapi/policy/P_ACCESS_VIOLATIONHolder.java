package org.csapi.policy;

/**
 *	Generated from IDL definition of exception "P_ACCESS_VIOLATION"
 *	@author JacORB IDL compiler 
 */

public final class P_ACCESS_VIOLATIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.P_ACCESS_VIOLATION value;

	public P_ACCESS_VIOLATIONHolder ()
	{
	}
	public P_ACCESS_VIOLATIONHolder(final org.csapi.policy.P_ACCESS_VIOLATION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.policy.P_ACCESS_VIOLATIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, value);
	}
}
