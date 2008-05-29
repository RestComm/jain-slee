package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceTypeProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypePropertyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceTypeProperty value;

	public TpServiceTypePropertyHolder ()
	{
	}
	public TpServiceTypePropertyHolder(final org.csapi.fw.TpServiceTypeProperty initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceTypePropertyHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceTypePropertyHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceTypePropertyHelper.write(_out, value);
	}
}
