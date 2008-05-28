package org.csapi;
/**
 *	Generated from IDL definition of union "TpSimpleAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpSimpleAttributeValueHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSimpleAttributeValue value;

	public TpSimpleAttributeValueHolder ()
	{
	}
	public TpSimpleAttributeValueHolder (final TpSimpleAttributeValue initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSimpleAttributeValueHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSimpleAttributeValueHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSimpleAttributeValueHelper.write (out, value);
	}
}
