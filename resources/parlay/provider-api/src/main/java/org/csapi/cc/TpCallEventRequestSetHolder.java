package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpCallEventRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventRequestSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallEventRequest[] value;

	public TpCallEventRequestSetHolder ()
	{
	}
	public TpCallEventRequestSetHolder (final org.csapi.cc.TpCallEventRequest[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallEventRequestSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallEventRequestSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallEventRequestSetHelper.write (out,value);
	}
}
