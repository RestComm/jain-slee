package org.csapi;

/**
 *	Generated from IDL definition of exception "P_UNAUTHORISED_PARAMETER_VALUE"
 *	@author JacORB IDL compiler 
 */

public final class P_UNAUTHORISED_PARAMETER_VALUEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_UNAUTHORISED_PARAMETER_VALUE value;

	public P_UNAUTHORISED_PARAMETER_VALUEHolder ()
	{
	}
	public P_UNAUTHORISED_PARAMETER_VALUEHolder(final org.csapi.P_UNAUTHORISED_PARAMETER_VALUE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.write(_out, value);
	}
}
