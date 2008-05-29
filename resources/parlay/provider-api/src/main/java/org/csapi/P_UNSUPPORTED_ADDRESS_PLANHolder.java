package org.csapi;

/**
 *	Generated from IDL definition of exception "P_UNSUPPORTED_ADDRESS_PLAN"
 *	@author JacORB IDL compiler 
 */

public final class P_UNSUPPORTED_ADDRESS_PLANHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_UNSUPPORTED_ADDRESS_PLAN value;

	public P_UNSUPPORTED_ADDRESS_PLANHolder ()
	{
	}
	public P_UNSUPPORTED_ADDRESS_PLANHolder(final org.csapi.P_UNSUPPORTED_ADDRESS_PLAN initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.write(_out, value);
	}
}
