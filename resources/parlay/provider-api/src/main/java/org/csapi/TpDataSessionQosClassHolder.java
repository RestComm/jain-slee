package org.csapi;
/**
 *	Generated from IDL definition of enum "TpDataSessionQosClass"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionQosClassHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionQosClass value;

	public TpDataSessionQosClassHolder ()
	{
	}
	public TpDataSessionQosClassHolder (final TpDataSessionQosClass initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionQosClassHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionQosClassHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionQosClassHelper.write (out,value);
	}
}
