package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagePriority"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagePriorityHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessagePriority value;

	public TpMessagePriorityHolder ()
	{
	}
	public TpMessagePriorityHolder (final TpMessagePriority initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessagePriorityHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessagePriorityHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessagePriorityHelper.write (out,value);
	}
}
