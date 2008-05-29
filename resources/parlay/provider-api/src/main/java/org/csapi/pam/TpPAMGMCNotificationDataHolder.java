package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMGMCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMGMCNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMGMCNotificationData value;

	public TpPAMGMCNotificationDataHolder ()
	{
	}
	public TpPAMGMCNotificationDataHolder(final org.csapi.pam.TpPAMGMCNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMGMCNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMGMCNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMGMCNotificationDataHelper.write(_out, value);
	}
}
