package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMultiMediaCallIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallIdentifierSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMultiMediaCallIdentifier[] value;

	public TpMultiMediaCallIdentifierSetHolder ()
	{
	}
	public TpMultiMediaCallIdentifierSetHolder (final org.csapi.cc.mmccs.TpMultiMediaCallIdentifier[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMultiMediaCallIdentifierSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMultiMediaCallIdentifierSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMultiMediaCallIdentifierSetHelper.write (out,value);
	}
}
