package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of struct "TpCallLegIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mpccs.TpCallLegIdentifier value;

	public TpCallLegIdentifierHolder ()
	{
	}
	public TpCallLegIdentifierHolder(final org.csapi.cc.mpccs.TpCallLegIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mpccs.TpCallLegIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mpccs.TpCallLegIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mpccs.TpCallLegIdentifierHelper.write(_out, value);
	}
}
