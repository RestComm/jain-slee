package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceProfile"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProfileHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceProfile value;

	public TpServiceProfileHolder ()
	{
	}
	public TpServiceProfileHolder(final org.csapi.fw.TpServiceProfile initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceProfileHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceProfileHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceProfileHelper.write(_out, value);
	}
}
