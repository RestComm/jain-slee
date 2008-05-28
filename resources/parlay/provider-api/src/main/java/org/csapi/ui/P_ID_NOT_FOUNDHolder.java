package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_ID_NOT_FOUND"
 *	@author JacORB IDL compiler 
 */

public final class P_ID_NOT_FOUNDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.P_ID_NOT_FOUND value;

	public P_ID_NOT_FOUNDHolder ()
	{
	}
	public P_ID_NOT_FOUNDHolder(final org.csapi.ui.P_ID_NOT_FOUND initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.P_ID_NOT_FOUNDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.P_ID_NOT_FOUNDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.P_ID_NOT_FOUNDHelper.write(_out, value);
	}
}
