package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_NOT_MEMBER"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_NOT_MEMBERHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_NOT_MEMBER value;

	public P_PAM_NOT_MEMBERHolder ()
	{
	}
	public P_PAM_NOT_MEMBERHolder(final org.csapi.pam.P_PAM_NOT_MEMBER initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_NOT_MEMBERHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_NOT_MEMBERHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_NOT_MEMBERHelper.write(_out, value);
	}
}
