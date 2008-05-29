package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMICNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMICNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMICNotificationData value;

	public TpPAMICNotificationDataHolder ()
	{
	}
	public TpPAMICNotificationDataHolder(final org.csapi.pam.TpPAMICNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMICNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMICNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMICNotificationDataHelper.write(_out, value);
	}
}
