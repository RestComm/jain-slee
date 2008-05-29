package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_VALIDITY_INFO"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_VALIDITY_INFOHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_VALIDITY_INFO value;

	public P_UNKNOWN_VALIDITY_INFOHolder ()
	{
	}
	public P_UNKNOWN_VALIDITY_INFOHolder(final org.csapi.cm.P_UNKNOWN_VALIDITY_INFO initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_VALIDITY_INFOHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_VALIDITY_INFOHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_VALIDITY_INFOHelper.write(_out, value);
	}
}
