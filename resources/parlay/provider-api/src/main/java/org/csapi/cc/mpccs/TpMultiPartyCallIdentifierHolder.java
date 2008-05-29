package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of struct "TpMultiPartyCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiPartyCallIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier value;

	public TpMultiPartyCallIdentifierHolder ()
	{
	}
	public TpMultiPartyCallIdentifierHolder(final org.csapi.cc.mpccs.TpMultiPartyCallIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.write(_out, value);
	}
}
