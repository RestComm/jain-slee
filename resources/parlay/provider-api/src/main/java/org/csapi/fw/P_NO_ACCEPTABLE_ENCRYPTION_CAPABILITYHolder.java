package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY"
 *	@author JacORB IDL compiler 
 */

public final class P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY value;

	public P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHolder ()
	{
	}
	public P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHolder(final org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITYHelper.write(_out, value);
	}
}
