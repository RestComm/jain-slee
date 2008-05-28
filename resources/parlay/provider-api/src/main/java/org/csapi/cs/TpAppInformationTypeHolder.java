package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpAppInformationType"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformationTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppInformationType value;

	public TpAppInformationTypeHolder ()
	{
	}
	public TpAppInformationTypeHolder (final TpAppInformationType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppInformationTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppInformationTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppInformationTypeHelper.write (out,value);
	}
}
