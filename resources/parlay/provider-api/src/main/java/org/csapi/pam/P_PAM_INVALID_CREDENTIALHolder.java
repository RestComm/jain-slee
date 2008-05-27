package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_INVALID_CREDENTIAL"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_INVALID_CREDENTIALHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_INVALID_CREDENTIAL value;

	public P_PAM_INVALID_CREDENTIALHolder ()
	{
	}
	public P_PAM_INVALID_CREDENTIALHolder(final org.csapi.pam.P_PAM_INVALID_CREDENTIAL initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, value);
	}
}
