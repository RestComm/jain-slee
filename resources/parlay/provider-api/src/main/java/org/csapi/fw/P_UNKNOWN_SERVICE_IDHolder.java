package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SERVICE_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SERVICE_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_UNKNOWN_SERVICE_ID value;

	public P_UNKNOWN_SERVICE_IDHolder ()
	{
	}
	public P_UNKNOWN_SERVICE_IDHolder(final org.csapi.fw.P_UNKNOWN_SERVICE_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.write(_out, value);
	}
}
