package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceProfileDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProfileDescriptionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceProfileDescription value;

	public TpServiceProfileDescriptionHolder ()
	{
	}
	public TpServiceProfileDescriptionHolder(final org.csapi.fw.TpServiceProfileDescription initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceProfileDescriptionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceProfileDescriptionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceProfileDescriptionHelper.write(_out, value);
	}
}
