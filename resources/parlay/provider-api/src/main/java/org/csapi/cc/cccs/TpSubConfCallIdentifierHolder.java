package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpSubConfCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpSubConfCallIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpSubConfCallIdentifier value;

	public TpSubConfCallIdentifierHolder ()
	{
	}
	public TpSubConfCallIdentifierHolder(final org.csapi.cc.cccs.TpSubConfCallIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpSubConfCallIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpSubConfCallIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpSubConfCallIdentifierHelper.write(_out, value);
	}
}
