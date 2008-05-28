package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAUNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAUNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAUNotificationData value;

	public TpPAMAUNotificationDataHolder ()
	{
	}
	public TpPAMAUNotificationDataHolder(final org.csapi.pam.TpPAMAUNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAUNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAUNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAUNotificationDataHelper.write(_out, value);
	}
}
