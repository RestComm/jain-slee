package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNKNOWN_GROUP"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNKNOWN_GROUPHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNKNOWN_GROUP value;

	public P_PAM_UNKNOWN_GROUPHolder ()
	{
	}
	public P_PAM_UNKNOWN_GROUPHolder(final org.csapi.pam.P_PAM_UNKNOWN_GROUP initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.write(_out, value);
	}
}
