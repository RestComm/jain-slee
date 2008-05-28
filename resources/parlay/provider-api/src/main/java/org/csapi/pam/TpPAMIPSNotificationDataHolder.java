package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMIPSNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIPSNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMIPSNotificationData value;

	public TpPAMIPSNotificationDataHolder ()
	{
	}
	public TpPAMIPSNotificationDataHolder(final org.csapi.pam.TpPAMIPSNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMIPSNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMIPSNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMIPSNotificationDataHelper.write(_out, value);
	}
}
