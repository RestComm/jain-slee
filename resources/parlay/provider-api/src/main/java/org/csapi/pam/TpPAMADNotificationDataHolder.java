package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMADNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMADNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMADNotificationData value;

	public TpPAMADNotificationDataHolder ()
	{
	}
	public TpPAMADNotificationDataHolder(final org.csapi.pam.TpPAMADNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMADNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMADNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMADNotificationDataHelper.write(_out, value);
	}
}
