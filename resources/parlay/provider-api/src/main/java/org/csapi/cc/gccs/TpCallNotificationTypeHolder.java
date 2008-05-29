package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallNotificationType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallNotificationType value;

	public TpCallNotificationTypeHolder ()
	{
	}
	public TpCallNotificationTypeHolder (final TpCallNotificationType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallNotificationTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallNotificationTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallNotificationTypeHelper.write (out,value);
	}
}
