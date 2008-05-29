package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_ASSIGNMENT"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_ASSIGNMENTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENT value;

	public P_PAM_UNKNOWN_ASSIGNMENTHolder ()
	{
	}
	public P_PAM_UNKNOWN_ASSIGNMENTHolder(final org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.write(_out, value);
	}
}
