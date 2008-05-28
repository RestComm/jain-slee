package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_SERVICE_NOT_ENABLED"
 *	@author JacORB IDL compiler 
 */

public final class P_SERVICE_NOT_ENABLEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_SERVICE_NOT_ENABLED value;

	public P_SERVICE_NOT_ENABLEDHolder ()
	{
	}
	public P_SERVICE_NOT_ENABLEDHolder(final org.csapi.fw.P_SERVICE_NOT_ENABLED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.write(_out, value);
	}
}
