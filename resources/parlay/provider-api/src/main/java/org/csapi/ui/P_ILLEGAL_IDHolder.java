package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_ILLEGAL_ID"
 *	@author JacORB IDL compiler 
 */

public final class P_ILLEGAL_IDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.P_ILLEGAL_ID value;

	public P_ILLEGAL_IDHolder ()
	{
	}
	public P_ILLEGAL_IDHolder(final org.csapi.ui.P_ILLEGAL_ID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.P_ILLEGAL_IDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.P_ILLEGAL_IDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.P_ILLEGAL_IDHelper.write(_out, value);
	}
}
