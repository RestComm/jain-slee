package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallNotificationRequest value;

	public TpCallNotificationRequestHolder ()
	{
	}
	public TpCallNotificationRequestHolder(final org.csapi.cc.TpCallNotificationRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallNotificationRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallNotificationRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallNotificationRequestHelper.write(_out, value);
	}
}
