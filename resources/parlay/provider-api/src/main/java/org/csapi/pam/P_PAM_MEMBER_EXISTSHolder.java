package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_MEMBER_EXISTS"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_MEMBER_EXISTSHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_MEMBER_EXISTS value;

	public P_PAM_MEMBER_EXISTSHolder ()
	{
	}
	public P_PAM_MEMBER_EXISTSHolder(final org.csapi.pam.P_PAM_MEMBER_EXISTS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_MEMBER_EXISTSHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_MEMBER_EXISTSHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_MEMBER_EXISTSHelper.write(_out, value);
	}
}
