package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_TEMPLATE_TYPE"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_TEMPLATE_TYPEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPE value;

	public P_UNKNOWN_TEMPLATE_TYPEHolder ()
	{
	}
	public P_UNKNOWN_TEMPLATE_TYPEHolder(final org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPEHelper.write(_out, value);
	}
}
