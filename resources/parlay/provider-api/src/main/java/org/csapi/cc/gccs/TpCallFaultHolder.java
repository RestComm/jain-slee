package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallFault"
 *	@author JacORB IDL compiler 
 */

public final class TpCallFaultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallFault value;

	public TpCallFaultHolder ()
	{
	}
	public TpCallFaultHolder (final TpCallFault initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallFaultHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallFaultHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallFaultHelper.write (out,value);
	}
}
