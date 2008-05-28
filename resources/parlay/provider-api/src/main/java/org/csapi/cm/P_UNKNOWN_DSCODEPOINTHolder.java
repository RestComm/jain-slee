package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_DSCODEPOINT"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_DSCODEPOINTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_DSCODEPOINT value;

	public P_UNKNOWN_DSCODEPOINTHolder ()
	{
	}
	public P_UNKNOWN_DSCODEPOINTHolder(final org.csapi.cm.P_UNKNOWN_DSCODEPOINT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_DSCODEPOINTHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_DSCODEPOINTHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_DSCODEPOINTHelper.write(_out, value);
	}
}
