package org.csapi.gms;

/**
 *	Generated from IDL definition of alias "TpMessageInfoPropertySet"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertySetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpMessageInfoProperty[] value;

	public TpMessageInfoPropertySetHolder ()
	{
	}
	public TpMessageInfoPropertySetHolder (final org.csapi.gms.TpMessageInfoProperty[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessageInfoPropertySetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessageInfoPropertySetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessageInfoPropertySetHelper.write (out,value);
	}
}
