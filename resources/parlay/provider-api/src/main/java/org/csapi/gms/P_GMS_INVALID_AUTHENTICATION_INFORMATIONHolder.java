package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_INVALID_AUTHENTICATION_INFORMATION"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_INVALID_AUTHENTICATION_INFORMATIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION value;

	public P_GMS_INVALID_AUTHENTICATION_INFORMATIONHolder ()
	{
	}
	public P_GMS_INVALID_AUTHENTICATION_INFORMATIONHolder(final org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.write(_out, value);
	}
}
