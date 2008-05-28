package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpClientAppDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpClientAppDescriptionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpClientAppDescription value;

	public TpClientAppDescriptionHolder ()
	{
	}
	public TpClientAppDescriptionHolder(final org.csapi.fw.TpClientAppDescription initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpClientAppDescriptionHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpClientAppDescriptionHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpClientAppDescriptionHelper.write(_out, value);
	}
}
