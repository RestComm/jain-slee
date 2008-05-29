package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpUserLocationSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserLocation[] value;

	public TpUserLocationSetHolder ()
	{
	}
	public TpUserLocationSetHolder (final org.csapi.mm.TpUserLocation[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUserLocationSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUserLocationSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUserLocationSetHelper.write (out,value);
	}
}
