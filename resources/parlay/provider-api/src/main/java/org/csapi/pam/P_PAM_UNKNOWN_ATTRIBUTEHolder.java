package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_ATTRIBUTE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_ATTRIBUTEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE value;

	public P_PAM_UNKNOWN_ATTRIBUTEHolder ()
	{
	}
	public P_PAM_UNKNOWN_ATTRIBUTEHolder(final org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, value);
	}
}
