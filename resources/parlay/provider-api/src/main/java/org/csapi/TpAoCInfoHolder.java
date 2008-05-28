package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAoCInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpAoCInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAoCInfo value;

	public TpAoCInfoHolder ()
	{
	}
	public TpAoCInfoHolder(final org.csapi.TpAoCInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpAoCInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpAoCInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpAoCInfoHelper.write(_out, value);
	}
}
