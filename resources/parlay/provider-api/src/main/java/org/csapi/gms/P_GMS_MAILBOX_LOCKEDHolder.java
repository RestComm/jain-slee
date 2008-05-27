package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_MAILBOX_LOCKED"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_MAILBOX_LOCKEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_MAILBOX_LOCKED value;

	public P_GMS_MAILBOX_LOCKEDHolder ()
	{
	}
	public P_GMS_MAILBOX_LOCKEDHolder(final org.csapi.gms.P_GMS_MAILBOX_LOCKED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.write(_out, value);
	}
}
