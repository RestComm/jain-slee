package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMediaNotificationRequestedSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaNotificationRequestedSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMediaNotificationRequested[] value;

	public TpMediaNotificationRequestedSetHolder ()
	{
	}
	public TpMediaNotificationRequestedSetHolder (final org.csapi.cc.mmccs.TpMediaNotificationRequested[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaNotificationRequestedSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaNotificationRequestedSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaNotificationRequestedSetHelper.write (out,value);
	}
}
