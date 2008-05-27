package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionFault"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionFaultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionFault value;

	public TpDataSessionFaultHolder ()
	{
	}
	public TpDataSessionFaultHolder (final TpDataSessionFault initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionFaultHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionFaultHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionFaultHelper.write (out,value);
	}
}
