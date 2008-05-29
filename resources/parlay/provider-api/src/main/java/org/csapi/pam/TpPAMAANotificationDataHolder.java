package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAANotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAANotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAANotificationData value;

	public TpPAMAANotificationDataHolder ()
	{
	}
	public TpPAMAANotificationDataHolder(final org.csapi.pam.TpPAMAANotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAANotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAANotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAANotificationDataHelper.write(_out, value);
	}
}
