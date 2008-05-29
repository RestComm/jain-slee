package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpAction"
 *	@author JacORB IDL compiler 
 */

public final class TpActionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAction value;

	public TpActionHolder ()
	{
	}
	public TpActionHolder (final TpAction initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpActionHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpActionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpActionHelper.write (out,value);
	}
}
