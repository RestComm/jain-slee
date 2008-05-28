package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpTrafficDirection"
 *	@author JacORB IDL compiler 
 */

public final class TpTrafficDirectionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpTrafficDirection value;

	public TpTrafficDirectionHolder ()
	{
	}
	public TpTrafficDirectionHolder (final TpTrafficDirection initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTrafficDirectionHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTrafficDirectionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTrafficDirectionHelper.write (out,value);
	}
}
