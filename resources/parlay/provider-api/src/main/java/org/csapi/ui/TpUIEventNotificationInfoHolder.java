package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventNotificationInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIEventNotificationInfo value;

	public TpUIEventNotificationInfoHolder ()
	{
	}
	public TpUIEventNotificationInfoHolder(final org.csapi.ui.TpUIEventNotificationInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUIEventNotificationInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUIEventNotificationInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUIEventNotificationInfoHelper.write(_out, value);
	}
}
