package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallAppInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAppInfoType value;

	public TpCallAppInfoTypeHolder ()
	{
	}
	public TpCallAppInfoTypeHolder (final TpCallAppInfoType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAppInfoTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAppInfoTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAppInfoTypeHelper.write (out,value);
	}
}
