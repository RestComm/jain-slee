package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceDescriptionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpServiceDescription value;

	public TpServiceDescriptionHolder ()
	{
	}
	public TpServiceDescriptionHolder(final org.csapi.fw.TpServiceDescription initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceDescriptionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceDescriptionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceDescriptionHelper.write(_out, value);
	}
}
