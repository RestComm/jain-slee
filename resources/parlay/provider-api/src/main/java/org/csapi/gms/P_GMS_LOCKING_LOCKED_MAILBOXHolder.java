package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_LOCKING_LOCKED_MAILBOX"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_LOCKING_LOCKED_MAILBOXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOX value;

	public P_GMS_LOCKING_LOCKED_MAILBOXHolder ()
	{
	}
	public P_GMS_LOCKING_LOCKED_MAILBOXHolder(final org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOX initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOXHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOXHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOXHelper.write(_out, value);
	}
}
