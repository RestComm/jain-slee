package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_UNAVAILABLE_INTERFACE"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_UNAVAILABLE_INTERFACEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACE value;

	public P_PAM_UNAVAILABLE_INTERFACEHolder ()
	{
	}
	public P_PAM_UNAVAILABLE_INTERFACEHolder(final org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACEHelper.write(_out, value);
	}
}
