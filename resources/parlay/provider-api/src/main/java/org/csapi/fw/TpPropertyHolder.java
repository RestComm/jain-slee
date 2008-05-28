package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpPropertyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpProperty value;

	public TpPropertyHolder ()
	{
	}
	public TpPropertyHolder(final org.csapi.fw.TpProperty initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpPropertyHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpPropertyHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpPropertyHelper.write(_out, value);
	}
}
