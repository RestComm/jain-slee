package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServicePropertyNameList"
 *	@author JacORB IDL compiler 
 */

public final class TpServicePropertyNameListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpServicePropertyNameListHolder ()
	{
	}
	public TpServicePropertyNameListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServicePropertyNameListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServicePropertyNameListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServicePropertyNameListHelper.write (out,value);
	}
}
