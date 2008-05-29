package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpUserLocationExtendedSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationExtendedSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserLocationExtended[] value;

	public TpUserLocationExtendedSetHolder ()
	{
	}
	public TpUserLocationExtendedSetHolder (final org.csapi.mm.TpUserLocationExtended[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUserLocationExtendedSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUserLocationExtendedSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUserLocationExtendedSetHelper.write (out,value);
	}
}
