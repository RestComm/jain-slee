package org.csapi.cc.cccs;
/**
 *	Generated from IDL definition of enum "TpVideoHandlingType"
 *	@author JacORB IDL compiler 
 */

public final class TpVideoHandlingTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpVideoHandlingType value;

	public TpVideoHandlingTypeHolder ()
	{
	}
	public TpVideoHandlingTypeHolder (final TpVideoHandlingType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpVideoHandlingTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpVideoHandlingTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpVideoHandlingTypeHelper.write (out,value);
	}
}
