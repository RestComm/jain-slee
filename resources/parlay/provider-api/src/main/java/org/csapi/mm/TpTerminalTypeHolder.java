package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpTerminalType"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpTerminalType value;

	public TpTerminalTypeHolder ()
	{
	}
	public TpTerminalTypeHolder (final TpTerminalType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTerminalTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTerminalTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTerminalTypeHelper.write (out,value);
	}
}
