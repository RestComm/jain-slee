package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpCallAdditionalErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalErrorInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAdditionalErrorInfo value;

	public TpCallAdditionalErrorInfoHolder ()
	{
	}
	public TpCallAdditionalErrorInfoHolder (final TpCallAdditionalErrorInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAdditionalErrorInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAdditionalErrorInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAdditionalErrorInfoHelper.write (out, value);
	}
}
