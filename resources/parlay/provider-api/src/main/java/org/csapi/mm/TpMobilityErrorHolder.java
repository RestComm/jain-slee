package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityError"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMobilityError value;

	public TpMobilityErrorHolder ()
	{
	}
	public TpMobilityErrorHolder (final TpMobilityError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMobilityErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMobilityErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMobilityErrorHelper.write (out,value);
	}
}
