package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallEventType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallEventType value;

	public TpCallEventTypeHolder ()
	{
	}
	public TpCallEventTypeHolder (final TpCallEventType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallEventTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallEventTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallEventTypeHelper.write (out,value);
	}
}
