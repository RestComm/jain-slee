package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpNotificationMediaRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationMediaRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpNotificationMediaRequest value;

	public TpNotificationMediaRequestHolder ()
	{
	}
	public TpNotificationMediaRequestHolder(final org.csapi.cc.mmccs.TpNotificationMediaRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.write(_out, value);
	}
}
