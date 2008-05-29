package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpServicePropertyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceProperty value;

	public TpServicePropertyHolder ()
	{
	}
	public TpServicePropertyHolder(final org.csapi.fw.TpServiceProperty initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServicePropertyHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServicePropertyHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServicePropertyHelper.write(_out, value);
	}
}
