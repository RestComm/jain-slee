package org.csapi.cc.mmccs;
/**
 *	Generated from IDL definition of enum "TpMediaStreamDirection"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDirectionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMediaStreamDirection value;

	public TpMediaStreamDirectionHolder ()
	{
	}
	public TpMediaStreamDirectionHolder (final TpMediaStreamDirection initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMediaStreamDirectionHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMediaStreamDirectionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMediaStreamDirectionHelper.write (out,value);
	}
}
