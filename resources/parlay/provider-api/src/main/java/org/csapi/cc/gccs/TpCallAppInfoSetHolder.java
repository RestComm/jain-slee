package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of alias "TpCallAppInfoSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallAppInfo[] value;

	public TpCallAppInfoSetHolder ()
	{
	}
	public TpCallAppInfoSetHolder (final org.csapi.cc.gccs.TpCallAppInfo[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAppInfoSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAppInfoSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAppInfoSetHelper.write (out,value);
	}
}
