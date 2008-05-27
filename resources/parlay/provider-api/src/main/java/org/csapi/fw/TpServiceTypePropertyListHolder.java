package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceTypePropertyList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceTypeProperty[] value;

	public TpServiceTypePropertyListHolder ()
	{
	}
	public TpServiceTypePropertyListHolder (final org.csapi.fw.TpServiceTypeProperty[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceTypePropertyListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceTypePropertyListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceTypePropertyListHelper.write (out,value);
	}
}
