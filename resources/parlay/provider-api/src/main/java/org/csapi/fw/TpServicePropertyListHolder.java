package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServicePropertyList"
 *	@author JacORB IDL compiler 
 */

public final class TpServicePropertyListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceProperty[] value;

	public TpServicePropertyListHolder ()
	{
	}
	public TpServicePropertyListHolder (final org.csapi.fw.TpServiceProperty[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServicePropertyListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServicePropertyListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServicePropertyListHelper.write (out,value);
	}
}
