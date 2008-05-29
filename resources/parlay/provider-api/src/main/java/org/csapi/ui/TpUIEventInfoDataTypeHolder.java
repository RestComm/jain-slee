package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIEventInfoDataType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventInfoDataTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIEventInfoDataType value;

	public TpUIEventInfoDataTypeHolder ()
	{
	}
	public TpUIEventInfoDataTypeHolder (final TpUIEventInfoDataType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIEventInfoDataTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIEventInfoDataTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIEventInfoDataTypeHelper.write (out,value);
	}
}
