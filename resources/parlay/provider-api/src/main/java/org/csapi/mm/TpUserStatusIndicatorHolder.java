package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpUserStatusIndicator"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusIndicatorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUserStatusIndicator value;

	public TpUserStatusIndicatorHolder ()
	{
	}
	public TpUserStatusIndicatorHolder (final TpUserStatusIndicator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUserStatusIndicatorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUserStatusIndicatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUserStatusIndicatorHelper.write (out,value);
	}
}
