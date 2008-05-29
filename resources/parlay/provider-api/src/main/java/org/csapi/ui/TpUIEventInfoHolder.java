package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIEventInfo value;

	public TpUIEventInfoHolder ()
	{
	}
	public TpUIEventInfoHolder(final org.csapi.ui.TpUIEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUIEventInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUIEventInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUIEventInfoHelper.write(_out, value);
	}
}
