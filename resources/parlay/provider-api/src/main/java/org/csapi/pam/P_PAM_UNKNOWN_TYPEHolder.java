package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_TYPEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_TYPE value;

	public P_PAM_UNKNOWN_TYPEHolder ()
	{
	}
	public P_PAM_UNKNOWN_TYPEHolder(final org.csapi.pam.P_PAM_UNKNOWN_TYPE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, value);
	}
}
