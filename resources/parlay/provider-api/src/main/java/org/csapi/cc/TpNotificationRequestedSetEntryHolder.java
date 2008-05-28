package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpNotificationRequestedSetEntry"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationRequestedSetEntryHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpNotificationRequestedSetEntry value;

	public TpNotificationRequestedSetEntryHolder ()
	{
	}
	public TpNotificationRequestedSetEntryHolder(final org.csapi.cc.TpNotificationRequestedSetEntry initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpNotificationRequestedSetEntryHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpNotificationRequestedSetEntryHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpNotificationRequestedSetEntryHelper.write(_out, value);
	}
}
