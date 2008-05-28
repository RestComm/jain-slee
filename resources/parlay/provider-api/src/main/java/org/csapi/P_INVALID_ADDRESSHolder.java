package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INVALID_ADDRESS"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ADDRESSHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INVALID_ADDRESS value;

	public P_INVALID_ADDRESSHolder ()
	{
	}
	public P_INVALID_ADDRESSHolder(final org.csapi.P_INVALID_ADDRESS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INVALID_ADDRESSHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INVALID_ADDRESSHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INVALID_ADDRESSHelper.write(_out, value);
	}
}
