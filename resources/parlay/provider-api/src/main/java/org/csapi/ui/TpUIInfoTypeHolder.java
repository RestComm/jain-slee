package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIInfoTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIInfoType value;

	public TpUIInfoTypeHolder ()
	{
	}
	public TpUIInfoTypeHolder (final TpUIInfoType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIInfoTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIInfoTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIInfoTypeHelper.write (out,value);
	}
}
