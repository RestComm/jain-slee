package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_MAILBOX_OPEN"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_MAILBOX_OPENHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_MAILBOX_OPEN value;

	public P_GMS_MAILBOX_OPENHolder ()
	{
	}
	public P_GMS_MAILBOX_OPENHolder(final org.csapi.gms.P_GMS_MAILBOX_OPEN initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_MAILBOX_OPENHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_MAILBOX_OPENHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_MAILBOX_OPENHelper.write(_out, value);
	}
}
