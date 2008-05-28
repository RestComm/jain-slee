package org.csapi.pam;
/**
 *	Generated from IDL definition of union "TpPAMNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMNotificationInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMNotificationInfo value;

	public TpPAMNotificationInfoHolder ()
	{
	}
	public TpPAMNotificationInfoHolder (final TpPAMNotificationInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMNotificationInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMNotificationInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMNotificationInfoHelper.write (out, value);
	}
}
