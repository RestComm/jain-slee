package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionEventInfo value;

	public TpDataSessionEventInfoHolder ()
	{
	}
	public TpDataSessionEventInfoHolder(final org.csapi.dsc.TpDataSessionEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionEventInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionEventInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionEventInfoHelper.write(_out, value);
	}
}
