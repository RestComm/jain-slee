package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_IS_CYCLIC"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_IS_CYCLICHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_IS_CYCLIC value;

	public P_PAM_IS_CYCLICHolder ()
	{
	}
	public P_PAM_IS_CYCLICHolder(final org.csapi.pam.P_PAM_IS_CYCLIC initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_IS_CYCLICHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_IS_CYCLICHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_IS_CYCLICHelper.write(_out, value);
	}
}
