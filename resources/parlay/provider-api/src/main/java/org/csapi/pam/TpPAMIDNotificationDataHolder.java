package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMIDNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIDNotificationDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMIDNotificationData value;

	public TpPAMIDNotificationDataHolder ()
	{
	}
	public TpPAMIDNotificationDataHolder(final org.csapi.pam.TpPAMIDNotificationData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMIDNotificationDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMIDNotificationDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMIDNotificationDataHelper.write(_out, value);
	}
}
