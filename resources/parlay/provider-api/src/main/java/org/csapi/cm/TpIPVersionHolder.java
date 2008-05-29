package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpIPVersion"
 *	@author JacORB IDL compiler 
 */

public final class TpIPVersionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpIPVersion value;

	public TpIPVersionHolder ()
	{
	}
	public TpIPVersionHolder (final TpIPVersion initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpIPVersionHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpIPVersionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpIPVersionHelper.write (out,value);
	}
}
