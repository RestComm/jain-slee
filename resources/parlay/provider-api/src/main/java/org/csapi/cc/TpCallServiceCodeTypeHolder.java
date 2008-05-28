package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallServiceCodeType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallServiceCodeType value;

	public TpCallServiceCodeTypeHolder ()
	{
	}
	public TpCallServiceCodeTypeHolder (final TpCallServiceCodeType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallServiceCodeTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallServiceCodeTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallServiceCodeTypeHelper.write (out,value);
	}
}
