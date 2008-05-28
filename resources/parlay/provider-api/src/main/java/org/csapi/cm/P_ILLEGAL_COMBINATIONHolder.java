package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_COMBINATION"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_COMBINATIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_ILLEGAL_COMBINATION value;

	public P_ILLEGAL_COMBINATIONHolder ()
	{
	}
	public P_ILLEGAL_COMBINATIONHolder(final org.csapi.cm.P_ILLEGAL_COMBINATION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.write(_out, value);
	}
}
