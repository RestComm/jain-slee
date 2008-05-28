package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationResponseIndicator"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationResponseIndicatorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLocationResponseIndicator value;

	public TpLocationResponseIndicatorHolder ()
	{
	}
	public TpLocationResponseIndicatorHolder (final TpLocationResponseIndicator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLocationResponseIndicatorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLocationResponseIndicatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLocationResponseIndicatorHelper.write (out,value);
	}
}
