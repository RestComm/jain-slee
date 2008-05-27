package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallError"
 *	@author JacORB IDL compiler 
 */

public final class TpCallErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallError value;

	public TpCallErrorHolder ()
	{
	}
	public TpCallErrorHolder(final org.csapi.cc.TpCallError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallErrorHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallErrorHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallErrorHelper.write(_out, value);
	}
}
