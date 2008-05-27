package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceTypeDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceTypeDescriptionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceTypeDescription value;

	public TpServiceTypeDescriptionHolder ()
	{
	}
	public TpServiceTypeDescriptionHolder(final org.csapi.fw.TpServiceTypeDescription initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceTypeDescriptionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceTypeDescriptionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceTypeDescriptionHelper.write(_out, value);
	}
}
