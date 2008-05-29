package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAVCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAVCNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAVCNotificationData value;

	public TpPAMAVCNotificationDataHolder ()
	{
	}
	public TpPAMAVCNotificationDataHolder(final org.csapi.pam.TpPAMAVCNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAVCNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAVCNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAVCNotificationDataHelper.write(_out, value);
	}
}
