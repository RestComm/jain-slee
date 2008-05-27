package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationType"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLocationType value;

	public TpLocationTypeHolder ()
	{
	}
	public TpLocationTypeHolder (final TpLocationType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLocationTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLocationTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLocationTypeHelper.write (out,value);
	}
}
