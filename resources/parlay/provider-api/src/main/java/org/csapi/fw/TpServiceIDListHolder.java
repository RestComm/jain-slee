package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpServiceIDList"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceIDListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpServiceIDListHolder ()
	{
	}
	public TpServiceIDListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceIDListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceIDListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceIDListHelper.write (out,value);
	}
}
