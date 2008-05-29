package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadLevel"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadLevelHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadLevel value;

	public TpLoadLevelHolder ()
	{
	}
	public TpLoadLevelHolder (final TpLoadLevel initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadLevelHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadLevelHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadLevelHelper.write (out,value);
	}
}
