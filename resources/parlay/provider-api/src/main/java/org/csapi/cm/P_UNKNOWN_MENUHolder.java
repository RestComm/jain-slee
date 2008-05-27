package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_MENU"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_MENUHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_MENU value;

	public P_UNKNOWN_MENUHolder ()
	{
	}
	public P_UNKNOWN_MENUHolder(final org.csapi.cm.P_UNKNOWN_MENU initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_MENUHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_MENUHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_MENUHelper.write(_out, value);
	}
}
