package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SITE_LOCATION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SITE_LOCATIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_SITE_LOCATION value;

	public P_UNKNOWN_SITE_LOCATIONHolder ()
	{
	}
	public P_UNKNOWN_SITE_LOCATIONHolder(final org.csapi.cm.P_UNKNOWN_SITE_LOCATION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.write(_out, value);
	}
}
