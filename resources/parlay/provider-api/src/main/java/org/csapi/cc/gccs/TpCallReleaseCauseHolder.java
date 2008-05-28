package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReleaseCauseHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallReleaseCause value;

	public TpCallReleaseCauseHolder ()
	{
	}
	public TpCallReleaseCauseHolder(final org.csapi.cc.gccs.TpCallReleaseCause initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallReleaseCauseHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallReleaseCauseHelper.write(_out, value);
	}
}
