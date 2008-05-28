package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_TEMPLATES"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_TEMPLATESHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_TEMPLATES value;

	public P_UNKNOWN_TEMPLATESHolder ()
	{
	}
	public P_UNKNOWN_TEMPLATESHolder(final org.csapi.cm.P_UNKNOWN_TEMPLATES initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_TEMPLATESHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_TEMPLATESHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_TEMPLATESHelper.write(_out, value);
	}
}
