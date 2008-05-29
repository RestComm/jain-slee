package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of alias "TpSubConfCallIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpSubConfCallIdentifierSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpSubConfCallIdentifier[] value;

	public TpSubConfCallIdentifierSetHolder ()
	{
	}
	public TpSubConfCallIdentifierSetHolder (final org.csapi.cc.cccs.TpSubConfCallIdentifier[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSubConfCallIdentifierSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSubConfCallIdentifierSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSubConfCallIdentifierSetHelper.write (out,value);
	}
}
