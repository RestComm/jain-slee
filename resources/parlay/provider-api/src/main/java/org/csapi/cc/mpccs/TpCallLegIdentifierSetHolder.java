package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of alias "TpCallLegIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegIdentifierSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mpccs.TpCallLegIdentifier[] value;

	public TpCallLegIdentifierSetHolder ()
	{
	}
	public TpCallLegIdentifierSetHolder (final org.csapi.cc.mpccs.TpCallLegIdentifier[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallLegIdentifierSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallLegIdentifierSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallLegIdentifierSetHelper.write (out,value);
	}
}
