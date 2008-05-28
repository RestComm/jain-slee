package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_ADDITION_TO_SAG"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ADDITION_TO_SAGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_ADDITION_TO_SAG value;

	public P_INVALID_ADDITION_TO_SAGHolder ()
	{
	}
	public P_INVALID_ADDITION_TO_SAGHolder(final org.csapi.fw.P_INVALID_ADDITION_TO_SAG initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.write(_out, value);
	}
}
