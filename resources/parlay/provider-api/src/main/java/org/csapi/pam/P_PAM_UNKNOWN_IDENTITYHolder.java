package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_IDENTITY"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_IDENTITYHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_IDENTITY value;

	public P_PAM_UNKNOWN_IDENTITYHolder ()
	{
	}
	public P_PAM_UNKNOWN_IDENTITYHolder(final org.csapi.pam.P_PAM_UNKNOWN_IDENTITY initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, value);
	}
}
