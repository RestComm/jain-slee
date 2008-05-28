package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessageInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertyNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessageInfoPropertyName value;

	public TpMessageInfoPropertyNameHolder ()
	{
	}
	public TpMessageInfoPropertyNameHolder (final TpMessageInfoPropertyName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessageInfoPropertyNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessageInfoPropertyNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessageInfoPropertyNameHelper.write (out,value);
	}
}
