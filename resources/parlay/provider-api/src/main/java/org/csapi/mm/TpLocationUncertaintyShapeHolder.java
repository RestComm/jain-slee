package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationUncertaintyShape"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationUncertaintyShapeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLocationUncertaintyShape value;

	public TpLocationUncertaintyShapeHolder ()
	{
	}
	public TpLocationUncertaintyShapeHolder (final TpLocationUncertaintyShape initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLocationUncertaintyShapeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLocationUncertaintyShapeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLocationUncertaintyShapeHelper.write (out,value);
	}
}
