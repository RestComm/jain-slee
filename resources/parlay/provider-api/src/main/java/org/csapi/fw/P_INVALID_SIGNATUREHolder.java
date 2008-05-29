package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_SIGNATURE"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SIGNATUREHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_SIGNATURE value;

	public P_INVALID_SIGNATUREHolder ()
	{
	}
	public P_INVALID_SIGNATUREHolder(final org.csapi.fw.P_INVALID_SIGNATURE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_SIGNATUREHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_SIGNATUREHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_SIGNATUREHelper.write(_out, value);
	}
}
