package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpPropertyList"
 *	@author JacORB IDL compiler 
 */

public final class TpPropertyListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpProperty[] value;

	public TpPropertyListHolder ()
	{
	}
	public TpPropertyListHolder (final org.csapi.fw.TpProperty[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPropertyListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPropertyListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPropertyListHelper.write (out,value);
	}
}
