package org.csapi;

/**
 *	Generated from IDL definition of alias "TpOctetSet"
 *	@author JacORB IDL compiler 
 */

public final class TpOctetSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public byte[] value;

	public TpOctetSetHolder ()
	{
	}
	public TpOctetSetHolder (final byte[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpOctetSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpOctetSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpOctetSetHelper.write (out,value);
	}
}
