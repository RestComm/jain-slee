package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_CRITERIA"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_CRITERIAHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_CRITERIA value;

	public P_INVALID_CRITERIAHolder ()
	{
	}
	public P_INVALID_CRITERIAHolder(final org.csapi.P_INVALID_CRITERIA initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_CRITERIAHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_CRITERIAHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_CRITERIAHelper.write(_out, value);
	}
}
