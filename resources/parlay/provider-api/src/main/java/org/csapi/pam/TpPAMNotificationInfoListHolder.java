package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMNotificationInfoList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMNotificationInfoListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMNotificationInfo[] value;

	public TpPAMNotificationInfoListHolder ()
	{
	}
	public TpPAMNotificationInfoListHolder (final org.csapi.pam.TpPAMNotificationInfo[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMNotificationInfoListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMNotificationInfoListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMNotificationInfoListHelper.write (out,value);
	}
}
