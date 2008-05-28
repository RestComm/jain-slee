package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_TYPE_ASSOCIATED"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_TYPE_ASSOCIATEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_TYPE_ASSOCIATED value;

	public P_PAM_TYPE_ASSOCIATEDHolder ()
	{
	}
	public P_PAM_TYPE_ASSOCIATEDHolder(final org.csapi.pam.P_PAM_TYPE_ASSOCIATED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.write(_out, value);
	}
}
