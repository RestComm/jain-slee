package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_ALIAS_NOT_UNIQUE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_ALIAS_NOT_UNIQUEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUE value;

	public P_PAM_ALIAS_NOT_UNIQUEHolder ()
	{
	}
	public P_PAM_ALIAS_NOT_UNIQUEHolder(final org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUEHelper.write(_out, value);
	}
}
