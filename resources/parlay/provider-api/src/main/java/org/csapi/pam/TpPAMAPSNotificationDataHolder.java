package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAPSNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAPSNotificationData value;

	public TpPAMAPSNotificationDataHolder ()
	{
	}
	public TpPAMAPSNotificationDataHolder(final org.csapi.pam.TpPAMAPSNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAPSNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAPSNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAPSNotificationDataHelper.write(_out, value);
	}
}
