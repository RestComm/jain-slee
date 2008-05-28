package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpCallServiceCodeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallServiceCode[] value;

	public TpCallServiceCodeSetHolder ()
	{
	}
	public TpCallServiceCodeSetHolder (final org.csapi.cc.TpCallServiceCode[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallServiceCodeSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallServiceCodeSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallServiceCodeSetHelper.write (out,value);
	}
}
