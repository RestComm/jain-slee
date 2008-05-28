package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityDiagnostic"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityDiagnosticHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMobilityDiagnostic value;

	public TpMobilityDiagnosticHolder ()
	{
	}
	public TpMobilityDiagnosticHolder (final TpMobilityDiagnostic initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMobilityDiagnosticHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMobilityDiagnosticHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMobilityDiagnosticHelper.write (out,value);
	}
}
