package org.csapi.dsc;
/**
 *	Generated from IDL definition of union "TpDataSessionAdditionalErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionAdditionalErrorInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionAdditionalErrorInfo value;

	public TpDataSessionAdditionalErrorInfoHolder ()
	{
	}
	public TpDataSessionAdditionalErrorInfoHolder (final TpDataSessionAdditionalErrorInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionAdditionalErrorInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionAdditionalErrorInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionAdditionalErrorInfoHelper.write (out, value);
	}
}
