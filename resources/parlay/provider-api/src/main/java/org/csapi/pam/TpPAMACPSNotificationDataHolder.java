package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMACPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACPSNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMACPSNotificationData value;

	public TpPAMACPSNotificationDataHolder ()
	{
	}
	public TpPAMACPSNotificationDataHolder(final org.csapi.pam.TpPAMACPSNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMACPSNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMACPSNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMACPSNotificationDataHelper.write(_out, value);
	}
}
