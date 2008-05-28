package org.csapi;

/**
 *	Generated from IDL definition of alias "TpAttributeList"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAttribute[] value;

	public TpAttributeListHolder ()
	{
	}
	public TpAttributeListHolder (final org.csapi.TpAttribute[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAttributeListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAttributeListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAttributeListHelper.write (out,value);
	}
}
