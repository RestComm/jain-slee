package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageStatusHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessageStatus value;

	public TpMessageStatusHolder ()
	{
	}
	public TpMessageStatusHolder (final TpMessageStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessageStatusHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessageStatusHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessageStatusHelper.write (out,value);
	}
}
