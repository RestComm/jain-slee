package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of alias "TpMultiPartyCallIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiPartyCallIdentifierSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] value;

	public TpMultiPartyCallIdentifierSetHolder ()
	{
	}
	public TpMultiPartyCallIdentifierSetHolder (final org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMultiPartyCallIdentifierSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMultiPartyCallIdentifierSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMultiPartyCallIdentifierSetHelper.write (out,value);
	}
}
