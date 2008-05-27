package org.csapi.cs;
/**
 *	Generated from IDL definition of union "TpAppInformation"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformationHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAppInformation value;

	public TpAppInformationHolder ()
	{
	}
	public TpAppInformationHolder (final TpAppInformation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppInformationHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppInformationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppInformationHelper.write (out, value);
	}
}
