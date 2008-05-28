package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_ALIAS"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_ALIASHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_ALIAS value;

	public P_PAM_UNKNOWN_ALIASHolder ()
	{
	}
	public P_PAM_UNKNOWN_ALIASHolder(final org.csapi.pam.P_PAM_UNKNOWN_ALIAS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_ALIASHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_ALIASHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_ALIASHelper.write(_out, value);
	}
}
