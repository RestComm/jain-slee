package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamEventType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamEventTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMediaStreamEventType value;

	public TpMediaStreamEventTypeHolder ()
	{
	}
	public TpMediaStreamEventTypeHolder (final TpMediaStreamEventType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaStreamEventTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaStreamEventTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaStreamEventTypeHelper.write (out,value);
	}
}
