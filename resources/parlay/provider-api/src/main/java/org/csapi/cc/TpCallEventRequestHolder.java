package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallEventRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallEventRequest value;

	public TpCallEventRequestHolder ()
	{
	}
	public TpCallEventRequestHolder(final org.csapi.cc.TpCallEventRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallEventRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallEventRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallEventRequestHelper.write(_out, value);
	}
}
