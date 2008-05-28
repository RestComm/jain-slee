package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpNotificationRequestedSet"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationRequestedSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpNotificationRequested[] value;

	public TpNotificationRequestedSetHolder ()
	{
	}
	public TpNotificationRequestedSetHolder (final org.csapi.cc.TpNotificationRequested[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpNotificationRequestedSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpNotificationRequestedSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpNotificationRequestedSetHelper.write (out,value);
	}
}
