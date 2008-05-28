package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamDataTypeRequestType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeRequestTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMediaStreamDataTypeRequestType value;

	public TpMediaStreamDataTypeRequestTypeHolder ()
	{
	}
	public TpMediaStreamDataTypeRequestTypeHolder (final TpMediaStreamDataTypeRequestType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaStreamDataTypeRequestTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaStreamDataTypeRequestTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaStreamDataTypeRequestTypeHelper.write (out,value);
	}
}
