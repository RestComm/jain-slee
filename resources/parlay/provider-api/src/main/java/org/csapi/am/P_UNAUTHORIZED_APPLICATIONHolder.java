package org.csapi.am;

/**
 *	Generated from IDL definition of exception "P_UNAUTHORIZED_APPLICATION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNAUTHORIZED_APPLICATIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.P_UNAUTHORIZED_APPLICATION value;

	public P_UNAUTHORIZED_APPLICATIONHolder ()
	{
	}
	public P_UNAUTHORIZED_APPLICATIONHolder(final org.csapi.am.P_UNAUTHORIZED_APPLICATION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.write(_out, value);
	}
}
