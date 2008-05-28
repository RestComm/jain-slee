package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMWCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMWCNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMWCNotificationData value;

	public TpPAMWCNotificationDataHolder ()
	{
	}
	public TpPAMWCNotificationDataHolder(final org.csapi.pam.TpPAMWCNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMWCNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMWCNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMWCNotificationDataHelper.write(_out, value);
	}
}
