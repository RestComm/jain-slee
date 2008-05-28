package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServicePropertyValueList"
 *	@author JacORB IDL compiler 
 */

public final class TpServicePropertyValueListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpServicePropertyValueListHolder ()
	{
	}
	public TpServicePropertyValueListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServicePropertyValueListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServicePropertyValueListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServicePropertyValueListHelper.write (out,value);
	}
}
