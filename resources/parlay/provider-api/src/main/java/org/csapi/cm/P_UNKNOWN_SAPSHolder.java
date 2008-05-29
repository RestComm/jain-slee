package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SAPS"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SAPSHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_SAPS value;

	public P_UNKNOWN_SAPSHolder ()
	{
	}
	public P_UNKNOWN_SAPSHolder(final org.csapi.cm.P_UNKNOWN_SAPS initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_SAPSHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_SAPSHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_SAPSHelper.write(_out, value);
	}
}
