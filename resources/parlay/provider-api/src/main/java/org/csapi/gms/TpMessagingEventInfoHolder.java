package org.csapi.gms;
/**
 *	Generated from IDL definition of union "TpMessagingEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessagingEventInfo value;

	public TpMessagingEventInfoHolder ()
	{
	}
	public TpMessagingEventInfoHolder (final TpMessagingEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessagingEventInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessagingEventInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessagingEventInfoHelper.write (out, value);
	}
}
