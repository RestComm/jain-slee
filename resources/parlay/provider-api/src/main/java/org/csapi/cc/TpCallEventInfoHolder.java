package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallEventInfo value;

	public TpCallEventInfoHolder ()
	{
	}
	public TpCallEventInfoHolder(final org.csapi.cc.TpCallEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallEventInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallEventInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallEventInfoHelper.write(_out, value);
	}
}
