package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_CAPABILITY"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_CAPABILITYHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY value;

	public P_PAM_UNKNOWN_CAPABILITYHolder ()
	{
	}
	public P_PAM_UNKNOWN_CAPABILITYHolder(final org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_CAPABILITYHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_CAPABILITYHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_CAPABILITYHelper.write(_out, value);
	}
}
