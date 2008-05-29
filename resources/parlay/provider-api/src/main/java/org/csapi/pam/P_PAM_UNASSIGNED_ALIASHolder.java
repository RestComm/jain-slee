package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNASSIGNED_ALIAS"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNASSIGNED_ALIASHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNASSIGNED_ALIAS value;

	public P_PAM_UNASSIGNED_ALIASHolder ()
	{
	}
	public P_PAM_UNASSIGNED_ALIASHolder(final org.csapi.pam.P_PAM_UNASSIGNED_ALIAS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNASSIGNED_ALIASHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNASSIGNED_ALIASHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNASSIGNED_ALIASHelper.write(_out, value);
	}
}
