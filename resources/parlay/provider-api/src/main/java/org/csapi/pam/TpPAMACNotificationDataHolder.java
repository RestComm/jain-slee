package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMACNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMACNotificationData value;

	public TpPAMACNotificationDataHolder ()
	{
	}
	public TpPAMACNotificationDataHolder(final org.csapi.pam.TpPAMACNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMACNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMACNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMACNotificationDataHelper.write(_out, value);
	}
}
