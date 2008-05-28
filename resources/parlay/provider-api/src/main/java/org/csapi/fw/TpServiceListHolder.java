package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpService[] value;

	public TpServiceListHolder ()
	{
	}
	public TpServiceListHolder (final org.csapi.fw.TpService[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceListHelper.write (out,value);
	}
}
