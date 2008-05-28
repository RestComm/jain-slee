package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpInterfaceNameList"
 *	@author JacORB IDL compiler 
 */

public final class TpInterfaceNameListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpInterfaceNameListHolder ()
	{
	}
	public TpInterfaceNameListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpInterfaceNameListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpInterfaceNameListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpInterfaceNameListHelper.write (out,value);
	}
}
