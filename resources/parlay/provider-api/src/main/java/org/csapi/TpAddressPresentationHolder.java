package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressPresentation"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressPresentationHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAddressPresentation value;

	public TpAddressPresentationHolder ()
	{
	}
	public TpAddressPresentationHolder (final TpAddressPresentation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAddressPresentationHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAddressPresentationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAddressPresentationHelper.write (out,value);
	}
}
