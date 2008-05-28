package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_VALUE"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_VALUEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_ILLEGAL_VALUE value;

	public P_ILLEGAL_VALUEHolder ()
	{
	}
	public P_ILLEGAL_VALUEHolder(final org.csapi.cm.P_ILLEGAL_VALUE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_ILLEGAL_VALUEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_ILLEGAL_VALUEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_ILLEGAL_VALUEHelper.write(_out, value);
	}
}
