package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_ACTIVITY_TEST_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ACTIVITY_TEST_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID value;

	public P_INVALID_ACTIVITY_TEST_IDHolder ()
	{
	}
	public P_INVALID_ACTIVITY_TEST_IDHolder(final org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.write(_out, value);
	}
}
