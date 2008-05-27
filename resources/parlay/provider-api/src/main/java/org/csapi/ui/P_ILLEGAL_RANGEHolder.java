package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_RANGE"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_RANGEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.P_ILLEGAL_RANGE value;

	public P_ILLEGAL_RANGEHolder ()
	{
	}
	public P_ILLEGAL_RANGEHolder(final org.csapi.ui.P_ILLEGAL_RANGE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.P_ILLEGAL_RANGEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.P_ILLEGAL_RANGEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.P_ILLEGAL_RANGEHelper.write(_out, value);
	}
}
