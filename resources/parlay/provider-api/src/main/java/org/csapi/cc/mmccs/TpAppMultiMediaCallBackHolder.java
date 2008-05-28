package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of union "TpAppMultiMediaCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallBackHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppMultiMediaCallBack value;

	public TpAppMultiMediaCallBackHolder ()
	{
	}
	public TpAppMultiMediaCallBackHolder (final TpAppMultiMediaCallBack initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppMultiMediaCallBackHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppMultiMediaCallBackHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppMultiMediaCallBackHelper.write (out, value);
	}
}
