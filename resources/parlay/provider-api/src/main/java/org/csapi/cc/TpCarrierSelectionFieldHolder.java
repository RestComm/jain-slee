package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCarrierSelectionField"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrierSelectionFieldHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCarrierSelectionField value;

	public TpCarrierSelectionFieldHolder ()
	{
	}
	public TpCarrierSelectionFieldHolder (final TpCarrierSelectionField initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCarrierSelectionFieldHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCarrierSelectionFieldHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCarrierSelectionFieldHelper.write (out,value);
	}
}
