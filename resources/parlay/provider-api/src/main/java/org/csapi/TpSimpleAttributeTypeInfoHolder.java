package org.csapi;
/**
 *	Generated from IDL definition of enum "TpSimpleAttributeTypeInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpSimpleAttributeTypeInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSimpleAttributeTypeInfo value;

	public TpSimpleAttributeTypeInfoHolder ()
	{
	}
	public TpSimpleAttributeTypeInfoHolder (final TpSimpleAttributeTypeInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSimpleAttributeTypeInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSimpleAttributeTypeInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSimpleAttributeTypeInfoHelper.write (out,value);
	}
}
