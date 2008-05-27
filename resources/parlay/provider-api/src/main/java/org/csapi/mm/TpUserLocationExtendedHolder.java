package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationExtended"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationExtendedHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserLocationExtended value;

	public TpUserLocationExtendedHolder ()
	{
	}
	public TpUserLocationExtendedHolder(final org.csapi.mm.TpUserLocationExtended initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpUserLocationExtendedHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpUserLocationExtendedHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpUserLocationExtendedHelper.write(_out, value);
	}
}
