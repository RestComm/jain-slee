package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_AGENT"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_AGENTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_AGENT value;

	public P_PAM_UNKNOWN_AGENTHolder ()
	{
	}
	public P_PAM_UNKNOWN_AGENTHolder(final org.csapi.pam.P_PAM_UNKNOWN_AGENT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, value);
	}
}
