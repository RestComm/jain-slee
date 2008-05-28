package org.csapi;
/**
 *	Generated from IDL definition of union "TpAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeValueHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAttributeValue value;

	public TpAttributeValueHolder ()
	{
	}
	public TpAttributeValueHolder (final TpAttributeValue initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAttributeValueHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAttributeValueHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAttributeValueHelper.write (out, value);
	}
}
