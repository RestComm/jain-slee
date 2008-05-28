package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_ATTRIBUTES"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_ATTRIBUTESHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES value;

	public P_PAM_UNKNOWN_ATTRIBUTESHolder ()
	{
	}
	public P_PAM_UNKNOWN_ATTRIBUTESHolder(final org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTESHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTESHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTESHelper.write(_out, value);
	}
}
