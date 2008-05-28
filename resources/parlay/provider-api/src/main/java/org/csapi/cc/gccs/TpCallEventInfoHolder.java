package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallEventInfo value;

	public TpCallEventInfoHolder ()
	{
	}
	public TpCallEventInfoHolder(final org.csapi.cc.gccs.TpCallEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallEventInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallEventInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallEventInfoHelper.write(_out, value);
	}
}
