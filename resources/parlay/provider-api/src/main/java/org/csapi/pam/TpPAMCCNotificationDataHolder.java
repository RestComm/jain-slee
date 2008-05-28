package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMCCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCCNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMCCNotificationData value;

	public TpPAMCCNotificationDataHolder ()
	{
	}
	public TpPAMCCNotificationDataHolder(final org.csapi.pam.TpPAMCCNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMCCNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMCCNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMCCNotificationDataHelper.write(_out, value);
	}
}
