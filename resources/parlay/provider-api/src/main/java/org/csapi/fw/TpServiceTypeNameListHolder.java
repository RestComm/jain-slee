package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceTypeNameList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypeNameListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpServiceTypeNameListHolder ()
	{
	}
	public TpServiceTypeNameListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceTypeNameListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceTypeNameListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceTypeNameListHelper.write (out,value);
	}
}
