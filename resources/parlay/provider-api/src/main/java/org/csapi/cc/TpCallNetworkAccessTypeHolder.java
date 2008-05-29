package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallNetworkAccessType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNetworkAccessTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallNetworkAccessType value;

	public TpCallNetworkAccessTypeHolder ()
	{
	}
	public TpCallNetworkAccessTypeHolder (final TpCallNetworkAccessType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallNetworkAccessTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallNetworkAccessTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallNetworkAccessTypeHelper.write (out,value);
	}
}
