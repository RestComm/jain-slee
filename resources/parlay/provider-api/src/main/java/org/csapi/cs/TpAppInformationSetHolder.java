package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpAppInformationSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformationSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpAppInformation[] value;

	public TpAppInformationSetHolder ()
	{
	}
	public TpAppInformationSetHolder (final org.csapi.cs.TpAppInformation[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppInformationSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppInformationSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppInformationSetHelper.write (out,value);
	}
}
