package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallBearerService"
 *	@author JacORB IDL compiler 
 */

public final class TpCallBearerServiceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallBearerService value;

	public TpCallBearerServiceHolder ()
	{
	}
	public TpCallBearerServiceHolder (final TpCallBearerService initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallBearerServiceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallBearerServiceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallBearerServiceHelper.write (out,value);
	}
}
