package org.csapi.policy;

/**
 *	Generated from IDL definition of exception "P_NO_TRANSACTION_IN_PROCESS"
 *	@author JacORB IDL compiler 
 */

public final class P_NO_TRANSACTION_IN_PROCESSHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS value;

	public P_NO_TRANSACTION_IN_PROCESSHolder ()
	{
	}
	public P_NO_TRANSACTION_IN_PROCESSHolder(final org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, value);
	}
}
