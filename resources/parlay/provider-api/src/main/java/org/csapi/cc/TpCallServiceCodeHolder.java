package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallServiceCode"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallServiceCode value;

	public TpCallServiceCodeHolder ()
	{
	}
	public TpCallServiceCodeHolder(final org.csapi.cc.TpCallServiceCode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallServiceCodeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallServiceCodeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallServiceCodeHelper.write(_out, value);
	}
}
