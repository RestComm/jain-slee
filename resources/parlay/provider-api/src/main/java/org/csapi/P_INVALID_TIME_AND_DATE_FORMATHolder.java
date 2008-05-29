package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_TIME_AND_DATE_FORMAT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_TIME_AND_DATE_FORMATHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_TIME_AND_DATE_FORMAT value;

	public P_INVALID_TIME_AND_DATE_FORMATHolder ()
	{
	}
	public P_INVALID_TIME_AND_DATE_FORMATHolder(final org.csapi.P_INVALID_TIME_AND_DATE_FORMAT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_TIME_AND_DATE_FORMATHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_TIME_AND_DATE_FORMATHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_TIME_AND_DATE_FORMATHelper.write(_out, value);
	}
}
