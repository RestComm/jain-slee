package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of union "TpMediaStreamDataTypeRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMediaStreamDataTypeRequest value;

	public TpMediaStreamDataTypeRequestHolder ()
	{
	}
	public TpMediaStreamDataTypeRequestHolder (final TpMediaStreamDataTypeRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaStreamDataTypeRequestHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaStreamDataTypeRequestHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaStreamDataTypeRequestHelper.write (out, value);
	}
}
