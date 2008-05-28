package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SITE_DESCRIPTION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SITE_DESCRIPTIONHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION value;

	public P_UNKNOWN_SITE_DESCRIPTIONHolder ()
	{
	}
	public P_UNKNOWN_SITE_DESCRIPTIONHolder(final org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.write(_out, value);
	}
}
