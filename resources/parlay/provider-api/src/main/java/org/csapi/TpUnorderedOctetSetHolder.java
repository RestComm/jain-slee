package org.csapi;

/**
 *	Generated from IDL definition of alias "TpUnorderedOctetSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUnorderedOctetSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public byte[] value;

	public TpUnorderedOctetSetHolder ()
	{
	}
	public TpUnorderedOctetSetHolder (final byte[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUnorderedOctetSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUnorderedOctetSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUnorderedOctetSetHelper.write (out,value);
	}
}
