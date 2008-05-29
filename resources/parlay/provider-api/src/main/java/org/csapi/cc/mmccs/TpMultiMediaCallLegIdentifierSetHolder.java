package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMultiMediaCallLegIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallLegIdentifierSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier[] value;

	public TpMultiMediaCallLegIdentifierSetHolder ()
	{
	}
	public TpMultiMediaCallLegIdentifierSetHolder (final org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMultiMediaCallLegIdentifierSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMultiMediaCallLegIdentifierSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMultiMediaCallLegIdentifierSetHelper.write (out,value);
	}
}
