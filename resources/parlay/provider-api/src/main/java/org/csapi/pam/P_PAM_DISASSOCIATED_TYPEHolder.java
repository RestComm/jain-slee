package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_DISASSOCIATED_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_DISASSOCIATED_TYPEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_DISASSOCIATED_TYPE value;

	public P_PAM_DISASSOCIATED_TYPEHolder ()
	{
	}
	public P_PAM_DISASSOCIATED_TYPEHolder(final org.csapi.pam.P_PAM_DISASSOCIATED_TYPE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_DISASSOCIATED_TYPEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_DISASSOCIATED_TYPEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_DISASSOCIATED_TYPEHelper.write(_out, value);
	}
}
