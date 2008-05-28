package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpService"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpService value;

	public TpServiceHolder ()
	{
	}
	public TpServiceHolder(final org.csapi.fw.TpService initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpServiceHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpServiceHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpServiceHelper.write(_out, value);
	}
}
