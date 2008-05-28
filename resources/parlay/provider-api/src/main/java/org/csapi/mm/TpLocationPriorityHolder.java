package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationPriority"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationPriorityHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLocationPriority value;

	public TpLocationPriorityHolder ()
	{
	}
	public TpLocationPriorityHolder (final TpLocationPriority initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLocationPriorityHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLocationPriorityHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLocationPriorityHelper.write (out,value);
	}
}
