package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_CANNOT_UNLOCK_MAILBOX"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_CANNOT_UNLOCK_MAILBOXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOX value;

	public P_GMS_CANNOT_UNLOCK_MAILBOXHolder ()
	{
	}
	public P_GMS_CANNOT_UNLOCK_MAILBOXHolder(final org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOX initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOXHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOXHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOXHelper.write(_out, value);
	}
}
