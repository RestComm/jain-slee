package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpSag"
 *	@author JacORB IDL compiler 
 */

public final class TpSagHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpSag value;

	public TpSagHolder ()
	{
	}
	public TpSagHolder(final org.csapi.fw.TpSag initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpSagHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpSagHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpSagHelper.write(_out, value);
	}
}
