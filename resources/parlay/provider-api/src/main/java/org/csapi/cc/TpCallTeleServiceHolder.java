package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallTeleService"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTeleServiceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallTeleService value;

	public TpCallTeleServiceHolder ()
	{
	}
	public TpCallTeleServiceHolder (final TpCallTeleService initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallTeleServiceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallTeleServiceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallTeleServiceHelper.write (out,value);
	}
}
