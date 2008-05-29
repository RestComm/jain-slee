package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpServiceTypePropertyMode"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyModeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpServiceTypePropertyMode value;

	public TpServiceTypePropertyModeHolder ()
	{
	}
	public TpServiceTypePropertyModeHolder (final TpServiceTypePropertyMode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpServiceTypePropertyModeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpServiceTypePropertyModeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpServiceTypePropertyModeHelper.write (out,value);
	}
}
