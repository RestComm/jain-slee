package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpVprpStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpVprpStatusHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpVprpStatus value;

	public TpVprpStatusHolder ()
	{
	}
	public TpVprpStatusHolder (final TpVprpStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpVprpStatusHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpVprpStatusHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpVprpStatusHelper.write (out,value);
	}
}
