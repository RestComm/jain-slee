package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpCallIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallIdentifier value;

	public TpCallIdentifierHolder ()
	{
	}
	public TpCallIdentifierHolder(final org.csapi.cc.gccs.TpCallIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallIdentifierHelper.write(_out, value);
	}
}
