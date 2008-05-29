package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceProfileIDList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProfileIDListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpServiceProfileIDListHolder ()
	{
	}
	public TpServiceProfileIDListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceProfileIDListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceProfileIDListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceProfileIDListHelper.write (out,value);
	}
}
