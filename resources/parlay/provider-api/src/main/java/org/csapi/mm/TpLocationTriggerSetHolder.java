package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpLocationTriggerSet"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpLocationTrigger[] value;

	public TpLocationTriggerSetHolder ()
	{
	}
	public TpLocationTriggerSetHolder (final org.csapi.mm.TpLocationTrigger[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLocationTriggerSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLocationTriggerSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLocationTriggerSetHelper.write (out,value);
	}
}
