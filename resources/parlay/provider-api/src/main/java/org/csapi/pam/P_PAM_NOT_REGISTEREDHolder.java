package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_NOT_REGISTERED"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_NOT_REGISTEREDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_NOT_REGISTERED value;

	public P_PAM_NOT_REGISTEREDHolder ()
	{
	}
	public P_PAM_NOT_REGISTEREDHolder(final org.csapi.pam.P_PAM_NOT_REGISTERED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.write(_out, value);
	}
}
