package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpInterfaceFault"
 *	@author JacORB IDL compiler 
 */

public final class TpInterfaceFaultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpInterfaceFault value;

	public TpInterfaceFaultHolder ()
	{
	}
	public TpInterfaceFaultHolder (final TpInterfaceFault initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpInterfaceFaultHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpInterfaceFaultHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpInterfaceFaultHelper.write (out,value);
	}
}
