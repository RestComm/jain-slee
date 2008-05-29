package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallNotificationInfo value;

	public TpCallNotificationInfoHolder ()
	{
	}
	public TpCallNotificationInfoHolder(final org.csapi.cc.TpCallNotificationInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallNotificationInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallNotificationInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallNotificationInfoHelper.write(_out, value);
	}
}
