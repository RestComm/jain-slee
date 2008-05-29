package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpAppMultiMediaCallBackRefType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallBackRefTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppMultiMediaCallBackRefType value;

	public TpAppMultiMediaCallBackRefTypeHolder ()
	{
	}
	public TpAppMultiMediaCallBackRefTypeHolder (final TpAppMultiMediaCallBackRefType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppMultiMediaCallBackRefTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppMultiMediaCallBackRefTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppMultiMediaCallBackRefTypeHelper.write (out,value);
	}
}
