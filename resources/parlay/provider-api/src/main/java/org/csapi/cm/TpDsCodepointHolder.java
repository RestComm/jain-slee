package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpDsCodepoint"
 *	@author JacORB IDL compiler 
 */

public final class TpDsCodepointHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpDsCodepoint value;

	public TpDsCodepointHolder ()
	{
	}
	public TpDsCodepointHolder(final org.csapi.cm.TpDsCodepoint initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpDsCodepointHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpDsCodepointHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpDsCodepointHelper.write(_out, value);
	}
}
