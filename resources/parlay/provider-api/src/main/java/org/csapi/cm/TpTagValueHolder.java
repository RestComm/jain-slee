package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpTagValue"
 *	@author JacORB IDL compiler 
 */

public final class TpTagValueHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpTagValue value;

	public TpTagValueHolder ()
	{
	}
	public TpTagValueHolder (final TpTagValue initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTagValueHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTagValueHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTagValueHelper.write (out,value);
	}
}
